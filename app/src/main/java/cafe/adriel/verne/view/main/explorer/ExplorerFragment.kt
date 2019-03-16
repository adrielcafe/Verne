package cafe.adriel.verne.view.main.explorer

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.ActionMode
import androidx.recyclerview.widget.LinearLayoutManager
import cafe.adriel.verne.R
import cafe.adriel.verne.extension.color
import cafe.adriel.verne.extension.colorFromAttr
import cafe.adriel.verne.extension.hideAnimated
import cafe.adriel.verne.extension.javaClass
import cafe.adriel.verne.extension.long
import cafe.adriel.verne.extension.setAnimatedState
import cafe.adriel.verne.extension.share
import cafe.adriel.verne.extension.showAnimated
import cafe.adriel.verne.model.ExplorerItem
import cafe.adriel.verne.util.AnalyticsUtil
import cafe.adriel.verne.util.StatefulLayoutController
import cafe.adriel.verne.view.BaseFragment
import cafe.adriel.verne.view.editor.EditorActivity
import cafe.adriel.verne.view.main.explorer.listener.ExplorerFragmentListener
import com.afollestad.assent.Permission
import com.afollestad.assent.runWithPermissions
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.files.folderChooser
import com.afollestad.materialdialogs.input.getInputLayout
import com.afollestad.materialdialogs.input.input
import com.afollestad.materialdialogs.list.listItems
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.mikepenz.fastadapter.commons.adapters.FastItemAdapter
import com.mikepenz.fastadapter.select.SelectExtension
import com.mikepenz.fastadapter_extensions.ActionModeHelper
import kotlinx.android.synthetic.main.fragment_explorer.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.android.viewmodel.ext.android.viewModel
import java.io.File

class ExplorerFragment : BaseFragment<ExplorerViewState>(), ActionModeHelper.ActionItemClickedListener {

    companion object {
        private const val FILE_NAME_MAX_LENGTH = 50
    }

    override val viewModel by viewModel<ExplorerViewModel>()

    private val adapter by lazy { FastItemAdapter<ExplorerAdapterItem>() }
    private val adapterSelectHelper by lazy { adapter.getExtension(javaClass<SelectExtension<ExplorerAdapterItem>>()) }
    private val adapterActionModeHelper by lazy { ActionModeHelper<ExplorerAdapterItem>(adapter, R.menu.main_action_mode, this) }

    private val stateLayoutCtrl by lazy { StatefulLayoutController.createController(requireContext()) }
    private val actionDelayMs by lazy { long(R.integer.action_delay) }

    lateinit var listener: ExplorerFragmentListener

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_explorer, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter.withSelectable(true)
            .withMultiSelect(true)
            .withSelectOnLongClick(true)
            .withOnPreLongClickListener { _, _, _, position ->
                val activity = requireActivity() as AppCompatActivity?
                when {
                    viewModel.searchMode -> false
                    activity != null -> adapterActionModeHelper.onLongClick(activity, position) != null
                    else -> false
                }
            }
            .withOnPreClickListener { _, _, item, _ ->
                when {
                    viewModel.searchMode -> false
                    else -> adapterActionModeHelper.onClick(item) ?: false
                }
            }
            .withOnClickListener { _, _, item, _ ->
                if (adapterActionModeHelper.isActive) {
                    true
                } else {
                    openItem(item.item)
                    false
                }
            }
            .withSelectionListener { _, _ ->
                adapterActionModeHelper.actionMode?.let {
                    updateActionModeMenu(it)
                }
            }
            .setHasStableIds(true)

        vStateLayout.setStateController(stateLayoutCtrl)
        stateLayoutCtrl.setAnimatedState(vStateLayout, StatefulLayoutController.STATE_PROGRESS)

        with(vItems) {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            adapter = this@ExplorerFragment.adapter
            itemAnimator = null
        }
        with(vExplorerFab) {
            findViewById<FloatingActionButton>(R.id.faboptions_fab).supportImageTintList = ColorStateList.valueOf(Color.WHITE)
            setOnClickListener {
                launch {
                    delay(actionDelayMs)
                    when (it.id) {
                        R.id.action_new_folder -> showNewItemDialog(true)
                        R.id.action_new_file -> showNewItemDialog(false)
                    }
                }
            }
        }

        if (savedInstanceState != null) {
            setSearchModeEnabled(viewModel.searchMode)
        }
    }

    override fun onStateUpdated(state: ExplorerViewState) {
        state.apply {
            items?.let { setItems(it) }
        }
    }

    override fun onClick(mode: ActionMode, menuItem: MenuItem): Boolean {
        val selectedItems = adapterSelectHelper?.selectedItems?.map { it.item }
        selectedItems?.also {
            when (menuItem.itemId) {
                R.id.action_print -> {
                    val item = it.firstOrNull()
                    if (item is ExplorerItem.File) {
                        printItem(item)
                    }
                }
                R.id.action_share -> {
                    val item = it.firstOrNull()
                    if (item is ExplorerItem.File) {
                        shareItem(item)
                    }
                }
                R.id.action_move -> showMoveItemsDialog(selectedItems)
                R.id.action_rename -> it.firstOrNull()?.let { item ->
                    showRenameItemDialog(item)
                }
                R.id.action_delete -> deleteItems(selectedItems)
            }
        }
        mode.finish()
        return true
    }

    private fun setItems(items: List<ExplorerItem>) {
        launch {
            val state = if (viewModel.searchMode && viewModel.isSearchQueryEmpty()) {
                StatefulLayoutController.STATE_EMPTY
            } else if (items.isEmpty()) {
                StatefulLayoutController.STATE_NOT_FOUND
            } else {
                StatefulLayoutController.STATE_CONTENT
            }
            stateLayoutCtrl.setAnimatedState(vStateLayout, state)
        }

        val scrollY = vItems.scrollY
        val adapterItems = items
            .sortedBy { it.title.toLowerCase() } // Secondary comparator
            .sortedBy { it is ExplorerItem.File } // Primary comparator
            .map { ExplorerAdapterItem(it, viewModel.searchMode) }
        adapter.set(adapterItems)
        vItems.scrollTo(0, scrollY)
    }

    private fun createItem(name: String, isFolder: Boolean = false) {
        launch {
            val newItem = viewModel.putItem(name, isFolder)
            openItem(newItem)
            if (isFolder) {
                AnalyticsUtil.logNewFolder()
            } else {
                AnalyticsUtil.logNewFile(AnalyticsUtil.NEW_FILE_SOURCE_INTERNAL)
            }
        }
    }

    private fun openItem(item: ExplorerItem) {
        listener.onItemOpened(item)
        when (item) {
            is ExplorerItem.Folder -> {
                viewModel.currentDir = item.file
            }
            is ExplorerItem.File -> context?.apply {
                EditorActivity.start(this, item)
            }
        }
    }

    private fun printItem(item: ExplorerItem.File) {
        launch {
            viewModel.getHtmlText(item)?.let {
                listener.onPrintHtml(item.title, it)
            }
        }
    }

    private fun shareItem(item: ExplorerItem.File) {
        activity?.apply {
            MaterialDialog(this).show {
                title(R.string.share_as)
                negativeButton(R.string.cancel)
                listItems(R.array.share_item_options, waitForPositiveButton = false) { _, index, _ ->
                    launch {
                        when (index) {
                            // Text
                            0 -> viewModel.getPlainText(item)?.share(this@apply)
                            // HTML text
                            1 -> viewModel.getHtmlText(item)?.share(this@apply, true)
                            // HTML file
                            2 -> item.file.share(this@apply)
                            // PDF file
                            3 -> viewModel.getPdfFile(item).share(this@apply)
                        }
                        AnalyticsUtil.logShare()
                    }
                }
            }
        }
    }

    private fun deleteItems(items: List<ExplorerItem>) {
        launch {
            view?.apply {
                val text = resources.getQuantityString(R.plurals.items_deleted, items.size, items.size)
                items.forEach { viewModel.softDeleteItem(it) }
                Snackbar.make(this, text, Snackbar.LENGTH_LONG)
                    .setActionTextColor(color(R.color.colorAccent))
                    .setAction(R.string.undo) {
                        this@ExplorerFragment.launch {
                            items.forEach { viewModel.restoreItem(it) }
                        }
                    }
                    .show()
            }
        }
    }

    private fun moveItems(items: List<ExplorerItem>, dir: File) {
        launch {
            items.forEach { viewModel.moveItem(it, dir) }
        }
    }

    fun search(query: String) {
        launch { viewModel.searchItems(query) }
    }

    fun setSearchModeEnabled(enabled: Boolean) {
        launch {
            viewModel.searchMode = enabled
            delay(actionDelayMs)
            if (enabled) {
                vExplorerFab.hideAnimated()
                if (vExplorerFab.isOpen) {
                    delay(actionDelayMs)
                    vExplorerFab.close(null)
                }
            } else {
                vExplorerFab.showAnimated()
            }
        }
    }

    private fun showNewItemDialog(isFolder: Boolean) {
        val title = if (isFolder) R.string.new_folder else R.string.new_file
        context?.apply {
            MaterialDialog(this).show {
                title(title)
                negativeButton(R.string.cancel)
                positiveButton(R.string.save)
                input(
                    maxLength = FILE_NAME_MAX_LENGTH,
                    inputType = InputType.TYPE_TEXT_FLAG_CAP_SENTENCES
                ) { _, value ->
                    createItem(value.toString(), isFolder)
                }
                getInputLayout().boxBackgroundColor = colorFromAttr(android.R.attr.colorBackgroundFloating)
            }
        }
    }

    private fun showRenameItemDialog(item: ExplorerItem) {
        context?.apply {
            MaterialDialog(this).show {
                title(R.string.rename)
                negativeButton(R.string.cancel)
                positiveButton(R.string.save)
                input(
                    maxLength = FILE_NAME_MAX_LENGTH,
                    inputType = InputType.TYPE_TEXT_FLAG_CAP_SENTENCES,
                    prefill = item.title
                ) { _, name ->
                    launch {
                        viewModel.renameItem(item, name.toString())
                    }
                }
                getInputLayout().boxBackgroundColor = colorFromAttr(android.R.attr.colorBackgroundFloating)
            }
        }
    }

    private fun showMoveItemsDialog(items: List<ExplorerItem>) = runWithPermissions(Permission.WRITE_EXTERNAL_STORAGE) {
        val filter = { file: File ->
            file.isDirectory && !file.isHidden
        }
        context?.apply {
            MaterialDialog(this).show {
                folderChooser(
                    viewModel.getBaseDir(),
                    filter,
                    emptyTextRes = R.string.nothing_here,
                    initialFolderLabel = R.string.app_name,
                    initialFolderAsRoot = true) { _, file ->
                    moveItems(items, file)
                }
            }
        }
    }

    private fun updateActionModeMenu(actionMode: ActionMode) {
        val selectedItems = adapterSelectHelper?.selectedItems
        val hasFolderItem = selectedItems?.firstOrNull { it.item is ExplorerItem.Folder } != null
        val isSingleItem = selectedItems?.size == 1
        val isSingleFileItem = isSingleItem && !hasFolderItem
        with(actionMode) {
            menu.findItem(R.id.action_print).isVisible = isSingleFileItem
            menu.findItem(R.id.action_share).isVisible = isSingleFileItem
            menu.findItem(R.id.action_rename).isVisible = isSingleItem
        }
    }

    fun backToPreviousFolder() = viewModel.goToParentDir()
}
