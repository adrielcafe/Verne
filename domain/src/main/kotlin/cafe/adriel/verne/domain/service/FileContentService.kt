package cafe.adriel.verne.domain.service

import cafe.adriel.verne.domain.model.ExplorerItem
import cafe.adriel.verne.domain.model.FileContent
import cafe.adriel.verne.domain.model.NewFileContent

interface FileContentService {

    suspend fun insert(file: ExplorerItem.File, newContent: NewFileContent)

    suspend fun get(file: ExplorerItem.File): Set<FileContent>
}
