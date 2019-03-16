package cafe.adriel.verne.view.main.explorer

import android.content.Context
import androidx.lifecycle.MutableLiveData
import cafe.adriel.verne.extension.asExplorerItem
import cafe.adriel.verne.model.ExplorerItem
import cafe.adriel.verne.repository.LocalExplorerRepository
import cafe.adriel.verne.util.CoroutineScopedStateViewModel
import cafe.adriel.verne.view.main.explorer.listener.ExplorerItemChangeListener
import com.uttampanchasara.pdfgenerator.CreatePdf
import kotlinx.coroutines.launch
import java.io.File
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class ExplorerViewModel(private val appContext: Context, private val explorerRepository: LocalExplorerRepository) : CoroutineScopedStateViewModel<ExplorerViewState>() {

    override val state = MutableLiveData<ExplorerViewState>()

    private lateinit var listener: ExplorerItemChangeListener

    private var searchQuery = ""
    var searchMode = false
        set(value) {
            field = value
            onSearchModeChange(searchMode)
        }
    var currentDir: File = explorerRepository.baseDir
        set(value) {
            field = value
            onCurrentDirChange(currentDir)
        }

    init {
        initState { ExplorerViewState() }
        refreshCurrentDir()
    }

    fun goToParentDir() = if (isBaseDir()) {
        false
    } else {
        currentDir = currentDir.parentFile
        true
    }

    fun getBaseDir() = explorerRepository.baseDir

    private fun isBaseDir() = currentDir == explorerRepository.baseDir

    private fun refreshCurrentDir() {
        onCurrentDirChange(currentDir)
    }

    private fun onSearchModeChange(enabled: Boolean) {
        if (enabled) {
            if (::listener.isInitialized) {
                listener.stopWatching()
            }
            updateState { it.copy(items = emptyList()) }
        } else {
            searchQuery = ""
            refreshCurrentDir()
        }
    }

    private fun onCurrentDirChange(dir: File) {
        if (::listener.isInitialized) {
            listener.stopWatching()
        }
        listener = ExplorerItemChangeListener(dir, true) {
            launch { selectItems(dir) }
        }
        listener.startWatching()
    }

    private suspend fun selectItems(dir: File) {
        // Check if is base dir or current dir, otherwise ignore
        if (currentDir == dir || currentDir == explorerRepository.baseDir) {
            updateState { it.copy(items = explorerRepository.select(dir)) }
        }
    }

    fun isSearchQueryEmpty() = searchQuery.isBlank()

    suspend fun searchItems(query: String) {
        searchQuery = query
        updateState { it.copy(items = explorerRepository.search(query)) }
    }

    suspend fun putItem(name: String, isFolder: Boolean): ExplorerItem {
        val fileName = if (!isFolder && !name.endsWith(".html", true)) {
            "$name.html"
        } else {
            name
        }
        val file = File(currentDir, fileName)
        val item = if (isFolder) ExplorerItem.Folder(file.path) else ExplorerItem.File(file.path)
        explorerRepository.create(item)
        return item
    }

    suspend fun moveItem(item: ExplorerItem, parentDir: File) {
        explorerRepository.move(item, parentDir)
    }

    suspend fun renameItem(item: ExplorerItem, newName: String) {
        explorerRepository.rename(item, newName)
    }

    suspend fun softDeleteItem(item: ExplorerItem) {
        if (!item.isDeleted) {
            explorerRepository.rename(item, ".${item.file.name}")
        }
    }

    suspend fun restoreItem(item: ExplorerItem) {
        val deletedFile = File(item.file.parent, ".${item.file.name}")
        if (item.isDeleted) {
            explorerRepository.rename(item, item.file.name.removePrefix("."))
        } else if (deletedFile.exists()) {
            explorerRepository.rename(deletedFile.asExplorerItem(), deletedFile.name.removePrefix("."))
        }
    }

    suspend fun getPlainText(item: ExplorerItem.File) = try {
        explorerRepository.getPlainText(item)
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }

    suspend fun getHtmlText(item: ExplorerItem.File) = try {
        explorerRepository.getHtmlText(item)
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }

    suspend fun getPdfFile(item: ExplorerItem.File): File = suspendCoroutine {
        launch {
            CreatePdf(appContext)
                .setPdfName(item.title)
                .setContent(getHtmlText(item))
                .setCallbackListener(object : CreatePdf.PdfCallbackListener {
                    override fun onSuccess(filePath: String) {
                        it.resume(File(filePath))
                    }
                    override fun onFailure(errorMsg: String) {
                        updateState { it.copy(exception = RuntimeException(errorMsg)) }
                    }
                })
                .create()
        }
    }
}
