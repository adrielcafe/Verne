package cafe.adriel.verne.data.internal.repository

import cafe.adriel.verne.data.internal.ktx.await
import cafe.adriel.verne.data.internal.mapper.data.NewItemToDataMapper
import cafe.adriel.verne.data.internal.mapper.domain.ItemToDomainMapper
import cafe.adriel.verne.data.internal.model.ItemDto
import cafe.adriel.verne.data.internal.model.ItemListDto
import cafe.adriel.verne.data.internal.utils.FileCounter
import cafe.adriel.verne.data.internal.utils.FileFinder
import cafe.adriel.verne.data.internal.utils.FirebasePathResolver
import cafe.adriel.verne.domain.model.ExplorerItem
import cafe.adriel.verne.domain.model.NewExplorerItem
import cafe.adriel.verne.domain.model.SearchResult
import cafe.adriel.verne.domain.service.ItemService
import cafe.adriel.verne.shared.logger.Logger
import com.google.common.collect.ConcurrentHashMultiset
import com.google.firebase.Timestamp
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.asExecutor
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow

internal class ItemRepository(
    private val firestore: FirebaseFirestore,
    private val fileFinder: FileFinder,
    private val fileCounter: FileCounter,
    private val pathResolver: FirebasePathResolver,
    private val logger: Logger
) : ItemService {

    private val itemsChangedChannel = ConflatedBroadcastChannel<Unit>()
    private val itemsCache = ConcurrentHashMultiset.create<ItemDto>()

    init {
        registerSnapshotListener()
    }

    override fun observeChanges(): Flow<Unit> =
        itemsChangedChannel.asFlow()

    override suspend fun insert(newItem: NewExplorerItem) {
        logger.d("Inserting item")

        itemsCache += NewItemToDataMapper(newItem)

        saveCachedItemsInFirestore()
    }

    override suspend fun update(item: ExplorerItem) {
        logger.d("Updating item")

        fetchItems()
            .firstOrNull { it.id == item.id }
            ?.also { itemsCache.remove(it) }
            ?.copy(
                name = item.name,
                parentId = item.parentId,
                updatedAt = Timestamp.now()
            )
            ?.let { itemsCache.add(it) }
            ?.also { saveCachedItemsInFirestore() }
    }

    override suspend fun softDelete(items: Set<ExplorerItem>) {
        logger.d("Soft deleting items -> ${items.size}")

        fetchItems()
            .map { itemResponse ->
                if (items.firstOrNull { item -> itemResponse.id == item.id } != null) {
                    itemResponse.copy(status = ItemDto.STATUS_DELETED)
                } else {
                    itemResponse
                }
            }
            .toList()
            .also(::updateCachedItems)

        saveCachedItemsInFirestore()
    }

    override suspend fun hardDelete(items: Set<ExplorerItem>) {
        logger.d("Hard deleting items -> ${items.size}")

        fetchItems()
            .filter { itemResponse -> items.firstOrNull { it.id == itemResponse.id } != null }
            .toSet()
            .also { itemsCache.removeAll(it) }

        saveCachedItemsInFirestore()
    }

    override suspend fun search(query: String): Set<SearchResult> {
        logger.d("Searching items")

        return if (query.isBlank()) emptySet()
        else fileFinder(query, fetchItems())
    }


    override suspend fun getFromRootFolder(): Set<ExplorerItem> {
        logger.d("Retrieving items -> root folder")

        return get(null)
    }

    override suspend fun getFromFolder(folder: ExplorerItem.Folder): Set<ExplorerItem> {
        logger.d("Retrieving items -> child folder")

        return get(folder.id)
    }

    private suspend fun get(parentId: String?): Set<ExplorerItem> =
        fetchItems()
            .filter { it.parentId == parentId && it.status == ItemDto.STATUS_EDITABLE }
            .associateWith { fileCounter(it, fetchItems()) }
            .map { (item, filesCount) ->
                ItemToDomainMapper(item).let {
                    when (it) {
                        is ExplorerItem.Folder -> it.copy(filesCount = filesCount)
                        is ExplorerItem.File -> it
                    }
                }
            }
            .toSet()

    private suspend fun fetchItems(): Sequence<ItemDto> =
        if (itemsCache.isNotEmpty()) itemsCache.asSequence()
        else fetchItemsFromFirestore()

    private suspend fun fetchItemsFromFirestore(): Sequence<ItemDto> {
        val path = pathResolver.getItemsPath()

        logger.d("Fetching items -> $path")

        return firestore
            .document(path)
            .get()
            .await()
            .toObject<ItemListDto>()
            ?.items
            ?.also(::updateCachedItems)
            ?.asSequence()
            ?: emptySequence()
    }

    private suspend fun saveCachedItemsInFirestore() {
        val path = pathResolver.getItemsPath()

        logger.d("Saving cached items -> $path")

        firestore.document(path)
            .set(ItemListDto(items = itemsCache.toList()))
            .await()
    }

    private fun updateCachedItems(items: List<ItemDto>) {
        logger.d("Updating cached items")

        itemsCache.apply {
            clear()
            addAll(items)
        }
    }

    private fun registerSnapshotListener() {
        logger.d("Registering items snapshot listener")

        firestore.document(pathResolver.getItemsPath())
            .addSnapshotListener(Dispatchers.IO.asExecutor(), EventListener { snapshot, exception ->
                snapshot?.let {
                    logger.d("Items snapshot changed -> ${snapshot.reference.path}")

                    snapshot
                        .toObject<ItemListDto>()
                        ?.items
                        ?.also(::updateCachedItems)

                    itemsChangedChannel.offer(Unit)
                }
                exception?.let { logger.e("Items snapshot listener error -> $exception", exception) }
            }).also {
                logger.d("Items snapshot listener registered")
            }
    }
}
