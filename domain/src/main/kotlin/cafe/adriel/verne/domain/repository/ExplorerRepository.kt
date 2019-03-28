package cafe.adriel.verne.domain.repository

import java.io.File

interface ExplorerRepository {

    suspend fun search(query: String, showDeleted: Boolean = false): Sequence<File>

    suspend fun select(dir: File, showDeleted: Boolean = false): Sequence<File>

    suspend fun create(file: File, isFolder: Boolean): Boolean

    suspend fun move(file: File, parentDir: File): File

    suspend fun rename(file: File, newName: String): File

    suspend fun getText(file: File): String

    suspend fun setText(file: File, text: String): Boolean
}
