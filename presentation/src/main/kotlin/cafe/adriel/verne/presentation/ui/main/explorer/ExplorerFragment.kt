package cafe.adriel.verne.presentation.ui.main.explorer

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
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import cafe.adriel.verne.domain.extension.asExplorerItem
import cafe.adriel.verne.domain.model.ExplorerItem
import cafe.adriel.verne.presentation.R
import cafe.adriel.verne.presentation.extension.color
import cafe.adriel.verne.presentation.extension.hideAnimated
import cafe.adriel.verne.presentation.extension.long
import cafe.adriel.verne.presentation.extension.setStateWithAnimation
import cafe.adriel.verne.presentation.extension.share
import cafe.adriel.verne.presentation.extension.showAnimated
import cafe.adriel.verne.presentation.extension.showSnackBar
import cafe.adriel.verne.presentation.helper.AnalyticsHelper
import cafe.adriel.verne.presentation.helper.StatefulLayoutHelper
import cafe.adriel.verne.presentation.ui.editor.EditorActivity
import cafe.adriel.verne.presentation.ui.main.explorer.listener.ExplorerFragmentListener
import cafe.adriel.verne.presentation.util.StateAware
import cafe.adriel.verne.shared.extension.javaClass
import cafe.adriel.verne.shared.extension.withDefault
import com.afollestad.assent.Permission
import com.afollestad.assent.runWithPermissions
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.files.folderChooser
import com.afollestad.materialdialogs.input.getInputField
import com.afollestad.materialdialogs.input.input
import com.afollestad.materialdialogs.list.listItems
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.mikepenz.fastadapter.ISelectionListener
import com.mikepenz.fastadapter.adapters.FastItemAdapter
import com.mikepenz.fastadapter.helpers.ActionModeHelper
import com.mikepenz.fastadapter.select.SelectExtension
import com.mikepenz.fastadapter.select.getSelectExtension
import kotlinx.android.synthetic.main.fragment_explorer.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File

internal class ExplorerFragment :
    Fragment(), StateAware<ExplorerViewState>, ActionModeHelper.ActionItemClickedListener {

    companion object {
        private const val FILE_NAME_MAX_LENGTH = 50
    }

    override val viewModel by viewModel<ExplorerViewModel>()
    private val analyticsHelper by inject<AnalyticsHelper>()
    private val statefulLayoutHelper by inject<StatefulLayoutHelper>()

    private val adapter by lazy { FastItemAdapter<ExplorerAdapterItem>() }
    private val adapterSelectHelper by lazy { adapter.getExtension(javaClass<SelectExtension<ExplorerAdapterItem>>()) }
    private val adapterActionModeHelper by lazy { ActionModeHelper(adapter, R.menu.main_action_mode, this) }

    private val actionDelayMs by lazy { long(R.integer.action_delay) }

    var listener: ExplorerFragmentListener? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.fragment_explorer, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        context?.apply {
            statefulLayoutHelper.init(this)
        }

        initAdapter()
        initViews()

        if (savedInstanceState != null) setSearchModeEnabled(viewModel.searchMode)

        viewModel.observeState(this, ::onStateUpdated)
    }

    override fun onStateUpdated(state: ExplorerViewState) {
        state.apply {
            items?.let { setItems(it) }
            exception?.let { activity?.showSnackBar(it.localizedMessage) }
        }
    }

    override fun onClick(mode: ActionMode, item: MenuItem): Boolean {
        val selectedItems = adapterSelectHelper?.selectedItems?.map { it.item }
        selectedItems?.also { items ->
            when (item.itemId) {
                R.id.action_print -> {
                    val explorerItem = items.firstOrNull()
                    if (explorerItem is ExplorerItem.File) printItem(explorerItem)
                }
                R.id.action_share -> {
                    val explorerItem = items.firstOrNull()
                    if (explorerItem is ExplorerItem.File) shareItem(explorerItem)
                }
                R.id.action_move -> showMoveItemsDialog(selectedItems)
                R.id.action_rename -> items.firstOrNull()?.let { item ->
                    showRenameItemDialog(item)
                }
                R.id.action_delete -> deleteItems(selectedItems)
            }
        }
        mode.finish()
        return true
    }

    private fun initAdapter() {
        adapter.apply {
            withUseIdDistributor(true)
            setHasStableIds(true)

            onPreLongClickListener = { _, _, _, position ->
                val activity = activity as? AppCompatActivity
                if (!viewModel.searchMode && activity != null) {
                    adapterActionModeHelper.onLongClick(activity, position) != null
                } else {
                    false
                }
            }

            onPreClickListener = { _, _, item, _ ->
                if (!viewModel.searchMode) {
                    adapterActionModeHelper.onClick(item) ?: false
                } else {
                    false
                }
            }

            onClickListener = { _, _, item, _ ->
                if (adapterActionModeHelper.isActive) {
                    true
                } else {
                    openItem(item.item)
                    false
                }
            }

            getSelectExtension().apply {
                isSelectable = true
                multiSelect = true
                selectOnLongClick = true
                selectionListener = object : ISelectionListener<ExplorerAdapterItem> {
                    override fun onSelectionChanged(item: ExplorerAdapterItem?, selected: Boolean) {
                        adapterActionModeHelper.actionMode?.let {
                            updateActionModeMenu(it)
                        }
                    }
                }
            }
        }
    }

    private fun initViews() {
        vStateLayout.setStateController(statefulLayoutHelper.controller)
        statefulLayoutHelper.controller.setStateWithAnimation(vStateLayout, StatefulLayoutHelper.STATE_PROGRESS)

        with(vItems) {
            setHasFixedSize(true)
            adapter = this@ExplorerFragment.adapter
            itemAnimator = null
        }
        with(vExplorerFab) {
            findViewById<FloatingActionButton>(R.id.faboptions_fab).supportImageTintList =
                ColorStateList.valueOf(Color.WHITE)
            setOnClickListener { view ->
                lifecycleScope.launch {
                    delay(actionDelayMs)
                    when (view.id) {
                        R.id.action_new_folder -> showNewItemDialog(true)
                        R.id.action_new_file -> showNewItemDialog(false)
                    }
                }
            }
        }
    }

    private fun setItems(items: List<ExplorerItem>) {
        val layoutState = when {
            viewModel.searchMode && viewModel.isSearchQueryEmpty() -> StatefulLayoutHelper.STATE_EMPTY
            items.isEmpty() -> StatefulLayoutHelper.STATE_NOT_FOUND
            else -> StatefulLayoutHelper.STATE_CONTENT
        }
        statefulLayoutHelper.controller.setStateWithAnimation(vStateLayout, layoutState)

        lifecycleScope.launch {
            val adapterItems = withDefault {
                items.asSequence()
                    .sortedBy { it.title.toLowerCase() } // Secondary comparator
                    .sortedBy { it is ExplorerItem.File } // Primary comparator
                    .map { ExplorerAdapterItem(lifecycleScope, it, viewModel.getBaseDir(), viewModel.searchMode) }
                    .toList()
            }
            val scrollY = vItems.scrollY
            adapter.set(adapterItems)
            vItems.scrollTo(0, scrollY)
        }
    }

    private fun createItem(name: String, isFolder: Boolean = false) {
        lifecycleScope.launch {
            val newItem = viewModel.putItem(name, isFolder)
            openItem(newItem)
            if (isFolder) {
                analyticsHelper.logNewFolder()
            } else {
                analyticsHelper.logNewFile(AnalyticsHelper.NEW_FILE_SOURCE_INTERNAL)
            }
        }
    }

    private fun openItem(item: ExplorerItem) {
        listener?.onItemOpened(item)
        when (item) {
            is ExplorerItem.Folder -> viewModel.currentDir = item.file
            is ExplorerItem.File -> context?.apply {
                EditorActivity.start(this, item)
            }
        }
    }

    private fun printItem(item: ExplorerItem.File) {
        if (item.isEmpty) {
            activity?.showSnackBar(R.string.file_empty)
            return
        }

        lifecycleScope.launch {
            val html = viewModel.getHtmlText(item)
            if (html.isEmpty()) {
                activity?.showSnackBar(R.string.file_empty)
            } else {
                listener?.onPrintHtml(item.title, html)
            }
        }
    }

    private fun shareItem(item: ExplorerItem.File) {
        if (item.isEmpty) {
            activity?.showSnackBar(R.string.file_empty)
            return
        }

        activity?.apply {
            MaterialDialog(this).show {
                title(R.string.share_as)
                negativeButton(R.string.cancel)
                listItems(R.array.share_item_options) { dialog, _, text ->
                    lifecycleScope.launch {
                        when (text) {
                            getString(R.string.text) -> viewModel.getPlainText(item).share(this@apply)
                            getString(R.string.html_text) -> viewModel.getHtmlText(item).share(this@apply, true)
                            getString(R.string.html_file) -> item.file.share(this@apply)
                            getString(R.string.pdf_file) -> viewModel.getPdfFile(item).share(this@apply)
                        }
                        analyticsHelper.logShare()
                    }
                    dialog.dismiss()
                }
            }
        }
    }

    private fun deleteItems(items: List<ExplorerItem>) {
        lifecycleScope.launch {
            view?.apply {
                val text = resources.getQuantityString(R.plurals.items_deleted, items.size, items.size)
                items.forEach { viewModel.softDeleteItem(it) }
                Snackbar.make(this, text, Snackbar.LENGTH_LONG)
                    .setActionTextColor(color(R.color.colorAccent))
                    .setAction(R.string.undo) {
                        lifecycleScope.launch {
                            items.forEach { viewModel.restoreItem(it) }
                        }
                    }
                    .show()
            }
        }
    }

    private fun moveItems(items: List<ExplorerItem>, folder: File) {
        lifecycleScope.launch {
            val item = folder.asExplorerItem()
            if (item is ExplorerItem.Folder) {
                items.forEach { viewModel.moveItem(it, item) }
            }
        }
    }

    fun search(query: String) {
        lifecycleScope.launch { viewModel.searchItems(query) }
    }

    fun setSearchModeEnabled(enabled: Boolean) {
        lifecycleScope.launch {
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
                getInputField().setBackgroundColor(Color.TRANSPARENT)
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
                    lifecycleScope.launch {
                        viewModel.renameItem(item, name.toString())
                    }
                }
                getInputField().setBackgroundColor(Color.TRANSPARENT)
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
                    initialFolderAsRoot = true
                ) { _, file ->
                    moveItems(items, file)
                }
            }
        }
    }

    private fun updateActionModeMenu(actionMode: ActionMode) {
        val selectedItems = adapterSelectHelper?.selectedItems
        val hasFolderItem = selectedItems?.any { it.item is ExplorerItem.Folder } ?: false
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
