package cafe.adriel.verne.repository

import android.content.Context
import androidx.core.text.HtmlCompat
import cafe.adriel.verne.App
import cafe.adriel.verne.extension.asExplorerItem
import cafe.adriel.verne.model.ExplorerItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

class LocalExplorerRepository(private val appContext: Context) : ExplorerRepository<ExplorerItem> {

    private val searchFilter = { file: File, query: String, showDeleted: Boolean ->
        file.isFile &&
        file.nameWithoutExtension.contains(query, true) &&
        (!file.isHidden || showDeleted)
    }
    private val selectFilter = { file: File, showDeleted: Boolean ->
        !file.isHidden || showDeleted
    }

    override val baseDir by lazy {
        File(appContext.filesDir, App.BASE_DIR_NAME).apply { mkdirs() }
    }

    override suspend fun search(query: String, showDeleted: Boolean) = withContext(Dispatchers.IO) {
        if (query.isBlank()) {
            emptyList()
        } else {
            baseDir.walk()
                .filter { searchFilter(it, query, showDeleted) }
                .map { it.asExplorerItem() }
                .toList()
        }
    }

    override suspend fun select(dir: File?, showDeleted: Boolean) = withContext(Dispatchers.IO) {
        with(dir ?: baseDir) {
            (listFiles() ?: emptyArray())
                .filter { selectFilter(it, showDeleted) }
                .map { it.asExplorerItem() }
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

    override suspend fun getPlainText(item: ExplorerItem) = withContext(Dispatchers.IO) {
        val html = getHtmlText(item)
        HtmlCompat.fromHtml(html, HtmlCompat.FROM_HTML_MODE_COMPACT).toString()
    }

    override suspend fun getHtmlText(item: ExplorerItem) = withContext(Dispatchers.IO) {
        item.file.readText()
    }
}
