package cafe.adriel.verne.presentation.ui.main.explorer

import android.content.Context
import androidx.core.text.HtmlCompat
import androidx.lifecycle.MutableLiveData
import cafe.adriel.verne.domain.extension.asExplorerItem
import cafe.adriel.verne.domain.model.BaseDir
import cafe.adriel.verne.domain.model.ExplorerItem
import cafe.adriel.verne.interactor.explorer.CreateItemExplorerInteractor
import cafe.adriel.verne.interactor.explorer.ItemTextExplorerInteractor
import cafe.adriel.verne.interactor.explorer.MoveItemExplorerInteractor
import cafe.adriel.verne.interactor.explorer.RenameItemExplorerInteractor
import cafe.adriel.verne.interactor.explorer.SearchItemsExplorerInteractor
import cafe.adriel.verne.interactor.explorer.SelectItemsExplorerInteractor
import cafe.adriel.verne.presentation.ui.main.explorer.listener.ExplorerItemChangeListener
import cafe.adriel.verne.presentation.util.CoroutineScopedStateViewModel
import com.uttampanchasara.pdfgenerator.CreatePdf
import kotlinx.coroutines.launch
import java.io.File
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class ExplorerViewModel(
    private val appContext: Context,
    private val baseDir: BaseDir,
    private val searchItemsInteractor: SearchItemsExplorerInteractor,
    private val selectItemsInteractor: SelectItemsExplorerInteractor,
    private val createItemInteractor: CreateItemExplorerInteractor,
    private val moveItemInteractor: MoveItemExplorerInteractor,
    private val renameItemInteractor: RenameItemExplorerInteractor,
    private val itemTextInteractor: ItemTextExplorerInteractor
) : CoroutineScopedStateViewModel<ExplorerViewState>() {

    override val state = MutableLiveData<ExplorerViewState>()

    private lateinit var listener: ExplorerItemChangeListener

    private var searchQuery = ""
    var searchMode = false
        set(value) {
            field = value
            onSearchModeChange(searchMode)
        }
    var currentDir: ExplorerItem.Folder = baseDir.item
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
        currentDir = ExplorerItem.Folder(currentDir.file.parent)
        true
    }

    fun getBaseDir() = baseDir

    fun isSearchQueryEmpty() = searchQuery.isBlank()

    private fun isBaseDir() = currentDir == baseDir.item

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

    private fun onCurrentDirChange(dir: ExplorerItem.Folder) {
        if (::listener.isInitialized) {
            listener.stopWatching()
        }
        listener = ExplorerItemChangeListener(dir.file, true) {
            launch {
                selectItems(dir)
            }
        }
        listener.startWatching()
    }

    private suspend fun selectItems(folder: ExplorerItem.Folder) {
        // Check if is base dir or current dir, otherwise ignore
        if (currentDir == folder || currentDir == baseDir.item) {
            updateState { it.copy(items = selectItemsInteractor(folder)) }
        }
    }

    suspend fun searchItems(query: String) {
        searchQuery = query
        updateState { it.copy(items = searchItemsInteractor(query)) }
    }

    suspend fun putItem(name: String, isFolder: Boolean): ExplorerItem {
        return createItemInteractor(currentDir, name, isFolder)
    }

    suspend fun moveItem(item: ExplorerItem, to: ExplorerItem.Folder) {
        moveItemInteractor(item, to)
    }

    suspend fun renameItem(item: ExplorerItem, newName: String) {
        renameItemInteractor(item, newName)
    }

    // TODO Move to Interactor
    suspend fun softDeleteItem(item: ExplorerItem) {
        if (!item.isDeleted) {
            renameItemInteractor(item, ".${item.file.name}")
        }
    }

    // TODO Move to Interactor
    suspend fun restoreItem(item: ExplorerItem) {
        val deletedFile = File(item.file.parent, ".${item.file.name}")
        if (item.isDeleted) {
            renameItemInteractor(item, item.file.name.removePrefix("."))
        } else if (deletedFile.exists()) {
            renameItemInteractor(deletedFile.asExplorerItem(), deletedFile.name.removePrefix("."))
        }
    }

    suspend fun getPlainText(item: ExplorerItem.File) = try {
        val html = getHtmlText(item)
        if(html != null) {
            HtmlCompat.fromHtml(html, HtmlCompat.FROM_HTML_MODE_COMPACT).toString()
        } else {
            null
        }
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }

    suspend fun getHtmlText(item: ExplorerItem.File) = try {
        itemTextInteractor.get(item)
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }

    suspend fun getPdfFile(item: ExplorerItem.File): File = suspendCoroutine {
        launch {
            getHtmlText(item)?.let { text ->
                CreatePdf(appContext)
                    .setPdfName(item.title)
                    .setContent(text)
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

}
