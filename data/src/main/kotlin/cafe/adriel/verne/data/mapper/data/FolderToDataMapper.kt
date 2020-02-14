package cafe.adriel.verne.data.mapper.data

import cafe.adriel.verne.data.mapper.Mapper
import cafe.adriel.verne.data.model.ExplorerFolder
import cafe.adriel.verne.domain.model.NewExplorerItem

internal object FolderToDataMapper : Mapper<NewExplorerItem.Folder, ExplorerFolder> {

    override suspend fun invoke(input: NewExplorerItem.Folder): ExplorerFolder = with(input) {
        ExplorerFolder(
            name = name
        ).also { folder ->
            parentFolderId?.let(folder.parent::setTargetId)
        }
    }
}
