package cafe.adriel.verne.data.mapper.domain

import cafe.adriel.verne.data.ktx.getPath
import cafe.adriel.verne.data.mapper.Mapper
import cafe.adriel.verne.data.model.ExplorerFile
import cafe.adriel.verne.domain.model.ExplorerItem
import java.util.Date

internal object FileToDomainMapper : Mapper<ExplorerFile, ExplorerItem.File> {

    override suspend fun invoke(input: ExplorerFile): ExplorerItem.File = with(input) {
        ExplorerItem.File(
            id = id,
            name = name,
            path = getPath(),
            content = content,
            deleted = deleted,
            createdAt = Date(createdAt),
            updatedAt = updatedAt?.let { Date(it) }
        )
    }
}
