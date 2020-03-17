package cafe.adriel.verne.data.internal.repository

import cafe.adriel.verne.data.internal.ktx.await
import cafe.adriel.verne.data.internal.mapper.data.NewFileContentToDataMapper
import cafe.adriel.verne.data.internal.mapper.domain.FileContentToDomainMapper
import cafe.adriel.verne.data.internal.model.FileContentDto
import cafe.adriel.verne.data.internal.model.FileContentListDto
import cafe.adriel.verne.data.internal.utils.FirebasePathResolver
import cafe.adriel.verne.domain.model.ExplorerItem
import cafe.adriel.verne.domain.model.FileContent
import cafe.adriel.verne.domain.model.NewFileContent
import cafe.adriel.verne.domain.service.FileContentService
import cafe.adriel.verne.shared.logger.Logger
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject

internal class FileContentRepository(
    private val firestore: FirebaseFirestore,
    private val pathResolver: FirebasePathResolver,
    private val logger: Logger
) : FileContentService {

    override suspend fun insert(file: ExplorerItem.File, newContent: NewFileContent) {
        val path = pathResolver.getFileContentPath(file.contentId)
        val updatedVersions = fetchFileContentsFromFirestore(file.contentId)
            .plus(NewFileContentToDataMapper(newContent))
            .sortedByDescending { it.createdAt }
            .take(MAX_VERSIONS)
            .toList()

        logger.d("Inserting new content to file -> $path")

        firestore.document(path)
            .set(FileContentListDto(versions = updatedVersions))
            .await()
    }

    override suspend fun get(file: ExplorerItem.File): Set<FileContent> {

        return fetchFileContentsFromFirestore(file.contentId)
            .map(FileContentToDomainMapper::invoke)
            .toSet()
    }

    private suspend fun fetchFileContentsFromFirestore(fileId: String): Sequence<FileContentDto> {
        val path = pathResolver.getFileContentPath(fileId)

        logger.d("Fetching file content -> $path")

        return firestore
            .document(path)
            .get()
            .await()
            .toObject<FileContentListDto>()
            ?.versions
            ?.asSequence()
            ?: emptySequence()
    }

    private companion object {

        const val MAX_VERSIONS = 10
    }
}
