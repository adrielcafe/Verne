package cafe.adriel.verne.data.mapper.domain

import cafe.adriel.verne.data.ktx.getFilesCount
import cafe.adriel.verne.data.ktx.getPath
import cafe.adriel.verne.data.mapper.Mapper
import cafe.adriel.verne.data.model.ExplorerFolder
import cafe.adriel.verne.domain.model.ExplorerItem
import java.util.Date

internal object FolderToDomainMapper : Mapper<ExplorerFolder, ExplorerItem.Folder> {

    override suspend fun invoke(input: ExplorerFolder): ExplorerItem.Folder = with(input) {
        ExplorerItem.Folder(
            id = id,
            name = name,
            path = getPath(),
            filesCount = getFilesCount(),
            deleted = deleted,
            createdAt = Date(createdAt),
            updatedAt = updatedAt?.let { Date(it) }
        )
    }
}
