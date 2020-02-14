package cafe.adriel.verne.domain.repository

import cafe.adriel.verne.domain.model.ExplorerItem
import cafe.adriel.verne.domain.model.NewExplorerItem

interface FolderRepository {

    suspend fun save(folder: NewExplorerItem.Folder)

    suspend fun delete(folder: ExplorerItem.Folder)

    suspend fun getItems(folder: ExplorerItem.Folder): List<ExplorerItem>

    suspend fun getRootItems(): List<ExplorerItem>
}
