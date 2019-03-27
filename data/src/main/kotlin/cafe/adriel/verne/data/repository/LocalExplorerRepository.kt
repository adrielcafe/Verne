package cafe.adriel.verne.data.repository

import cafe.adriel.verne.domain.extension.asExplorerItem
import cafe.adriel.verne.domain.model.BaseDir
import cafe.adriel.verne.domain.model.ExplorerItem
import cafe.adriel.verne.domain.repository.ExplorerRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

class LocalExplorerRepository(override val baseDir: BaseDir) : ExplorerRepository {

    init {
        baseDir.file.mkdirs()
    }

    private val searchFilter = { file: File, query: String, showDeleted: Boolean ->
        file.isFile &&
        file.nameWithoutExtension.contains(query, true) &&
        (!file.isHidden || showDeleted)
    }
    private val selectFilter = { file: File, showDeleted: Boolean ->
        !file.isHidden || showDeleted
    }

    override suspend fun search(query: String, showDeleted: Boolean) = withContext(Dispatchers.IO) {
        if (query.isBlank()) {
            emptyList()
        } else {
            baseDir.file.walk()
                .asSequence()
                .filter { searchFilter(it, query, showDeleted) }
                .map { it.asExplorerItem() }
                .toList()
        }
    }

    override suspend fun select(dir: File?, showDeleted: Boolean) = withContext(Dispatchers.IO) {
        with(dir ?: baseDir.file) {
            (listFiles() ?: emptyArray())
                .asSequence()
                .filter { selectFilter(it, showDeleted) }
                .map { it.asExplorerItem() }
                .toList()
        }
    }

    override suspend fun create(item: ExplorerItem) = withContext(Dispatchers.IO) {
        when (item) {
            is ExplorerItem.Folder -> item.file.mkdirs()
            is ExplorerItem.File -> item.file.createNewFile()
        }
    }

    override suspend fun move(item: ExplorerItem, parentDir: File) = withContext(Dispatchers.IO) {
        File(parentDir, item.file.name).also {
            item.file.renameTo(it)
        }
    }

    override suspend fun rename(item: ExplorerItem, newName: String) = withContext(Dispatchers.IO) {
        val newFileName = if (item is ExplorerItem.File && !newName.endsWith(".html", true)) {
            "$newName.html"
        } else {
            newName
        }
        File(item.file.parent, newFileName).also {
            item.file.renameTo(it)
        }
    }

    override suspend fun getHtmlText(item: ExplorerItem) = withContext(Dispatchers.IO) {
        item.file.readText()
    }
}
