package cafe.adriel.verne.repository

import java.io.File

interface ExplorerRepository<T> {

    val baseDir: File

    suspend fun search(query: String, showDeleted: Boolean = false): List<T>

    suspend fun select(dir: File? = null, showDeleted: Boolean = false): List<T>

    suspend fun create(item: T): Boolean

    suspend fun move(item: T, parentDir: File): File

    suspend fun rename(item: T, newName: String): File

    suspend fun getPlainText(item: T): String

    suspend fun getHtmlText(item: T): String

}