package cafe.adriel.verne.data.repository

import cafe.adriel.verne.data.mapper.data.FolderToDataMapper
import cafe.adriel.verne.data.mapper.domain.FileToDomainMapper
import cafe.adriel.verne.data.mapper.domain.FolderToDomainMapper
import cafe.adriel.verne.data.model.ExplorerFile
import cafe.adriel.verne.data.model.ExplorerFile_
import cafe.adriel.verne.data.model.ExplorerFolder
import cafe.adriel.verne.data.model.ExplorerFolder_
import cafe.adriel.verne.domain.model.ExplorerItem
import cafe.adriel.verne.domain.model.NewExplorerItem
import cafe.adriel.verne.domain.repository.FolderRepository
import io.objectbox.Box
import io.objectbox.kotlin.query

internal class ExplorerFolderRepository(
    private val folderBox: Box<ExplorerFolder>,
    private val fileBox: Box<ExplorerFile>
) : FolderRepository {

    override suspend fun save(folder: NewExplorerItem.Folder) {
        FolderToDataMapper(folder)
            .let(folderBox::put)
    }

    override suspend fun delete(folder: ExplorerItem.Folder) {
        folderBox.remove(folder.id)
    }

    override suspend fun getItems(folder: ExplorerItem.Folder): List<ExplorerItem> =
        folderBox.get(folder.id)
            .run {
                childFolders.map { FolderToDomainMapper(it) } +
                    childFiles.map { FileToDomainMapper(it) }
            }

    override suspend fun getRootItems(): List<ExplorerItem> =
        getRootFolders() + getRootFiles()

    private suspend fun getRootFolders(): List<ExplorerItem.Folder> =
        folderBox.query { ExplorerFolder_.parentId.isNull }
            .find()
            .map { FolderToDomainMapper(it) }

    private suspend fun getRootFiles(): List<ExplorerItem.File> =
        fileBox.query { ExplorerFile_.parentId.isNull }
            .find()
            .map { FileToDomainMapper(it) }
}
