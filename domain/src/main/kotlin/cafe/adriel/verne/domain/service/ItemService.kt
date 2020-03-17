package cafe.adriel.verne.domain.service

import cafe.adriel.verne.domain.model.ExplorerItem
import cafe.adriel.verne.domain.model.NewExplorerItem
import cafe.adriel.verne.domain.model.SearchResult
import kotlinx.coroutines.flow.Flow

interface ItemService {

    fun observeChanges(): Flow<Unit>

    suspend fun insert(newItem: NewExplorerItem)

    suspend fun update(item: ExplorerItem)

    suspend fun softDelete(items: Set<ExplorerItem>)

    suspend fun hardDelete(items: Set<ExplorerItem>)

    suspend fun search(query: String): Set<SearchResult>

    suspend fun getFromRootFolder(): Set<ExplorerItem>

    suspend fun getFromFolder(folder: ExplorerItem.Folder): Set<ExplorerItem>
}
