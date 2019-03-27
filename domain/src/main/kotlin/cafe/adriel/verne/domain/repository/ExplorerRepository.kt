package cafe.adriel.verne.domain.repository

import cafe.adriel.verne.domain.model.BaseDir
import cafe.adriel.verne.domain.model.ExplorerItem
import java.io.File

interface ExplorerRepository {

    val baseDir: BaseDir

    suspend fun search(query: String, showDeleted: Boolean = false): List<ExplorerItem>

    suspend fun select(dir: File? = null, showDeleted: Boolean = false): List<ExplorerItem>

    suspend fun create(item: ExplorerItem): Boolean

    suspend fun move(item: ExplorerItem, parentDir: File): File

    suspend fun rename(item: ExplorerItem, newName: String): File

    suspend fun getHtmlText(item: ExplorerItem): String
}
