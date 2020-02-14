package cafe.adriel.verne.data.repository

import cafe.adriel.verne.data.mapper.data.FileToDataMapper
import cafe.adriel.verne.data.mapper.domain.FileToDomainMapper
import cafe.adriel.verne.data.model.ExplorerFile
import cafe.adriel.verne.data.model.ExplorerFile_
import cafe.adriel.verne.domain.model.ExplorerItem
import cafe.adriel.verne.domain.model.NewExplorerItem
import cafe.adriel.verne.domain.repository.FileRepository
import io.objectbox.Box
import io.objectbox.kotlin.query

internal class ExplorerFileRepository(private val fileBox: Box<ExplorerFile>) : FileRepository {

    override suspend fun save(file: NewExplorerItem.File) {
        FileToDataMapper(file)
            .let(fileBox::put)
    }

    override suspend fun delete(file: ExplorerItem.File) {
        fileBox.remove(file.id)
    }

    override suspend fun search(query: String): List<ExplorerItem.File> =
        fileBox.query { ExplorerFile_.name.contains(query.toLowerCase()) }
            .find()
            .map { FileToDomainMapper(it) }
}
