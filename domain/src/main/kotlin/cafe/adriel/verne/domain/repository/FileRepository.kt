package cafe.adriel.verne.domain.repository

import cafe.adriel.verne.domain.model.ExplorerItem
import cafe.adriel.verne.domain.model.NewExplorerItem

interface FileRepository {

    suspend fun save(file: NewExplorerItem.File)

    suspend fun delete(file: ExplorerItem.File)

    suspend fun search(query: String): List<ExplorerItem.File>
}
