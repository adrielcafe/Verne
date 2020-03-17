package cafe.adriel.verne.data.internal.utils

import cafe.adriel.verne.data.internal.mapper.domain.ItemToDomainMapper
import cafe.adriel.verne.data.internal.model.ItemDto
import cafe.adriel.verne.domain.model.ExplorerItem
import cafe.adriel.verne.domain.model.SearchResult
import cafe.adriel.verne.domain.model.VerneConfig
import cafe.adriel.verne.shared.ktx.normalized

internal class FileFinder(private val config: VerneConfig) {

    operator fun invoke(query: String, items: Sequence<ItemDto>): Set<SearchResult> =
        items
            .filter { it.type == ItemDto.TYPE_FILE && it.status == ItemDto.STATUS_EDITABLE }
            .map(ItemToDomainMapper::invoke)
            .filterIsInstance<ExplorerItem.File>()
            .filter { it.matchesQuery(query) }
            .associateWith { getPath(it.parentId, items) }
            .map { (item, path) ->
                SearchResult(item, path)
            }
            .toSet()

    private fun ExplorerItem.File.matchesQuery(query: String): Boolean =
        name.normalized.contains(query.normalized, ignoreCase = true)

    private tailrec fun getPath(parentId: String?, items: Sequence<ItemDto>, currentPath: String = ""): String {
        val parentItem = parentId?.let { id ->
            items.firstOrNull { it.id == id }
        }

        return when {
            parentItem == null -> "/${config.appName}$currentPath"
            parentItem.parentId == null -> "/${config.appName}/${parentItem.name}$currentPath"
            else -> getPath(parentItem.parentId, items, "/${parentItem.name}$currentPath")
        }
    }
}
