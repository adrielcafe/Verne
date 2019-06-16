package cafe.adriel.verne.presentation.ui.main.explorer

import android.content.Context
import androidx.core.text.HtmlCompat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import cafe.adriel.verne.domain.extension.asExplorerItem
import cafe.adriel.verne.domain.interactor.explorer.CreateItemExplorerInteractor
import cafe.adriel.verne.domain.interactor.explorer.ItemTextExplorerInteractor
import cafe.adriel.verne.domain.interactor.explorer.MoveItemExplorerInteractor
import cafe.adriel.verne.domain.interactor.explorer.RenameItemExplorerInteractor
import cafe.adriel.verne.domain.interactor.explorer.RestoreItemExplorerInteractor
import cafe.adriel.verne.domain.interactor.explorer.SearchItemsExplorerInteractor
import cafe.adriel.verne.domain.interactor.explorer.SelectItemsExplorerInteractor
import cafe.adriel.verne.domain.interactor.explorer.SoftDeleteItemExplorerInteractor
import cafe.adriel.verne.domain.model.ExplorerItem
import cafe.adriel.verne.presentation.ui.main.explorer.listener.ExplorerItemChangeListener
import cafe.adriel.verne.shared.model.AppConfig
import com.etiennelenhart.eiffel.viewmodel.StateViewModel
import com.uttampanchasara.pdfgenerator.CreatePdf
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import java.io.File
import kotlin.coroutines.resume

internal class ExplorerViewModel(
    private val appContext: Context,
    private val appConfig: AppConfig,
    private val itemTextInteractor: ItemTextExplorerInteractor,
    private val searchItemsInteractor: SearchItemsExplorerInteractor,
    private val selectItemsInteractor: SelectItemsExplorerInteractor,
    private val createItemInteractor: CreateItemExplorerInteractor,
    private val moveItemInteractor: MoveItemExplorerInteractor,
    private val renameItemInteractor: RenameItemExplorerInteractor,
    private val softDeleteItemInteractor: SoftDeleteItemExplorerInteractor,
    private val restoreItemInteractor: RestoreItemExplorerInteractor
) : StateViewModel<ExplorerViewState>() {

    override val state = MutableLiveData<ExplorerViewState>()

    private var listener: ExplorerItemChangeListener? = null

    private var searchQuery = ""
    var searchMode = false
        set(value) {
            field = value
            onSearchModeChange(searchMode)
        }
    var currentDir: File = appConfig.explorerRootFolder
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

    fun getBaseDir() = appConfig.explorerRootFolder

    fun isSearchQueryEmpty() = searchQuery.isBlank()

    private fun isBaseDir() = currentDir == appConfig.explorerRootFolder

    private fun refreshCurrentDir() {
        onCurrentDirChange(currentDir)
    }

    private fun onSearchModeChange(enabled: Boolean) {
        if (enabled) {
            listener?.stopWatching()
            updateState { it.copy(items = emptyList()) }
        } else {
            searchQuery = ""
            refreshCurrentDir()
        }
    }

    private fun onCurrentDirChange(dir: File) {
        listener?.stopWatching()
        listener = ExplorerItemChangeListener(dir, true) {
            viewModelScope.launch {
                selectItems(dir)
            }
        }.also { it.startWatching() }
    }

    private suspend fun selectItems(folder: File) {
        // Check if is base dir or current dir, otherwise ignore
        if (currentDir == folder || currentDir == appConfig.explorerRootFolder) {
            val item = folder.asExplorerItem()
            if (item is ExplorerItem.Folder) {
                updateState { it.copy(items = selectItemsInteractor(item)) }
            }
        }
    }

    suspend fun searchItems(query: String) {
        searchQuery = query
        updateState { it.copy(items = searchItemsInteractor(query)) }
    }

    suspend fun putItem(name: String, isFolder: Boolean): ExplorerItem {
        val dirItem = currentDir.asExplorerItem()
        if (dirItem is ExplorerItem.Folder) {
            return createItemInteractor(dirItem, name, isFolder)
        } else {
            throw IllegalStateException("${currentDir.path} is not a valid folder")
        }
    }

    suspend fun moveItem(item: ExplorerItem, to: ExplorerItem.Folder) {
        moveItemInteractor(item, to)
    }

    suspend fun renameItem(item: ExplorerItem, newName: String) {
        renameItemInteractor(item, newName)
    }

    suspend fun softDeleteItem(item: ExplorerItem) {
        softDeleteItemInteractor(item)
    }

    suspend fun restoreItem(item: ExplorerItem) {
        restoreItemInteractor(item)
    }

    suspend fun getPlainText(item: ExplorerItem.File) =
        HtmlCompat.fromHtml(
            getHtmlText(item),
            HtmlCompat.FROM_HTML_MODE_COMPACT
        ).toString()

    suspend fun getHtmlText(item: ExplorerItem.File) = itemTextInteractor.get(item)

    suspend fun getPdfFile(item: ExplorerItem.File): File = suspendCancellableCoroutine { continuation ->
        viewModelScope.launch {
            CreatePdf(appContext)
                .setPdfName(item.title)
                .setContent(getHtmlText(item))
                .setCallbackListener(object : CreatePdf.PdfCallbackListener {
                    override fun onSuccess(filePath: String) = continuation.resume(File(filePath))

                    override fun onFailure(errorMsg: String) {
                        updateState {
                            it.copy(exception = RuntimeException(errorMsg))
                        }
                        continuation.cancel(RuntimeException(errorMsg))
                    }
                })
                .create()
        }
    }
}
