package cafe.adriel.verne.data.mapper.data

import cafe.adriel.verne.data.mapper.Mapper
import cafe.adriel.verne.data.model.ExplorerFile
import cafe.adriel.verne.domain.model.NewExplorerItem

internal object FileToDataMapper : Mapper<NewExplorerItem.File, ExplorerFile> {

    override suspend fun invoke(input: NewExplorerItem.File): ExplorerFile = with(input) {
        ExplorerFile(
            name = name,
            content = content
        ).also { file ->
            parentFolderId?.let(file.parent::setTargetId)
        }
    }
}
