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
import cafe.adriel.androidcoroutinescopes.appcompat.CoroutineScopedFragment
import cafe.adriel.verne.domain.extension.asExplorerItem
import cafe.adriel.verne.domain.model.ExplorerItem
import cafe.adriel.verne.presentation.R
import cafe.adriel.verne.presentation.extension.color
import cafe.adriel.verne.presentation.extension.colorFromAttr
import cafe.adriel.verne.presentation.extension.hideAnimated
import cafe.adriel.verne.presentation.extension.long
import cafe.adriel.verne.presentation.extension.setAnimatedState
import cafe.adriel.verne.presentation.extension.share
import cafe.adriel.verne.presentation.extension.showAnimated
import cafe.adriel.verne.presentation.helper.AnalyticsHelper
import cafe.adriel.verne.presentation.helper.StatefulLayoutHelper
import cafe.adriel.verne.presentation.ui.editor.EditorActivity
import cafe.adriel.verne.presentation.ui.main.explorer.listener.ExplorerFragmentListener
import cafe.adriel.verne.presentation.util.StateAware
import cafe.adriel.verne.shared.extension.javaClass
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
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File

class ExplorerFragment :
    CoroutineScopedFragment(), StateAware<ExplorerViewState>, ActionModeHelper.ActionItemClickedListener {

    companion object {
        private const val FILE_NAME_MAX_LENGTH = 50
    }

    override val viewModel by viewModel<ExplorerViewModel>()
    private val analyticsHelper by inject<AnalyticsHelper>()
    private val statefulLayoutHelper by inject<StatefulLayoutHelper>()

    private val adapter by lazy { FastItemAdapter<ExplorerAdapterItem>() }
    private val adapterSelectHelper by lazy { adapter.getExtension(javaClass<SelectExtension<ExplorerAdapterItem>>()) }
    private val adapterActionModeHelper by lazy {
        ActionModeHelper<ExplorerAdapterItem>(adapter, R.menu.main_action_mode, this)
    }

    private val actionDelayMs by lazy { long(R.integer.action_delay) }

    lateinit var listener: ExplorerFragmentListener

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_explorer, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        context?.apply {
            statefulLayoutHelper.init(this)
        }

        initAdapter()

        vStateLayout.setStateController(statefulLayoutHelper.controller)
        statefulLayoutHelper.controller.setAnimatedState(vStateLayout, StatefulLayoutHelper.STATE_PROGRESS)

        with(vItems) {
            setHasFixedSize(true)
            adapter = this@ExplorerFragment.adapter
            itemAnimator = null
        }
        with(vExplorerFab) {
            findViewById<FloatingActionButton>(R.id.faboptions_fab).supportImageTintList =
                ColorStateList.valueOf(Color.WHITE)
            setOnClickListener { view ->
                launch {
                    delay(actionDelayMs)
                    when (view.id) {
                        R.id.action_new_folder -> showNewItemDialog(true)
                        R.id.action_new_file -> showNewItemDialog(false)
                    }
                }
            }
        }

        if (savedInstanceState != null) {
            setSearchModeEnabled(viewModel.searchMode)
        }

        viewModel.observeState(this, ::onStateUpdated)
    }

    override fun onStateUpdated(state: ExplorerViewState) {
        state.apply {
            items?.let { setItems(it) }
            exception?.let { Snackbar.make(vStateLayout, it.localizedMessage, Snackbar.LENGTH_SHORT).show() }
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

    private fun initAdapter() {
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
    }

    private fun setItems(items: List<ExplorerItem>) {
        launch {
            val state = if (viewModel.searchMode && viewModel.isSearchQueryEmpty()) {
                StatefulLayoutHelper.STATE_EMPTY
            } else if (items.isEmpty()) {
                StatefulLayoutHelper.STATE_NOT_FOUND
            } else {
                StatefulLayoutHelper.STATE_CONTENT
            }
            statefulLayoutHelper.controller.setAnimatedState(vStateLayout, state)
        }

        val scrollY = vItems.scrollY
        val adapterItems = items
            .asSequence()
            .sortedBy { it.title.toLowerCase() } // Secondary comparator
            .sortedBy { it is ExplorerItem.File } // Primary comparator
            .map { ExplorerAdapterItem(it, viewModel.getBaseDir(), viewModel.searchMode) }
            .toList()
        adapter.set(adapterItems)
        vItems.scrollTo(0, scrollY)
    }

    private fun createItem(name: String, isFolder: Boolean = false) {
        launch {
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
        listener.onItemOpened(item)
        when (item) {
            is ExplorerItem.Folder -> viewModel.currentDir = item
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
        if (item.isEmpty) {
            Snackbar.make(vStateLayout, R.string.file_empty, Snackbar.LENGTH_SHORT).show()
            return
        }

        activity?.apply {
            MaterialDialog(this).show {
                title(R.string.share_as)
                negativeButton(R.string.cancel)
                listItems(R.array.share_item_options) { dialog, index, _ ->
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
                        analyticsHelper.logShare()
                    }
                    dialog.dismiss()
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

    private fun moveItems(items: List<ExplorerItem>, folder: File) {
        launch {
            val item = folder.asExplorerItem()
            if (item is ExplorerItem.Folder) {
                items.forEach { viewModel.moveItem(it, item) }
            }
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
                // TODO check if works on API <= 22
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
                // TODO check if works on API <= 22
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
                    viewModel.getBaseDir().item.file,
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
