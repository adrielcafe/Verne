package cafe.adriel.verne.presentation.ui.editor

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import android.widget.HorizontalScrollView
import android.widget.LinearLayout
import androidx.core.app.NavUtils
import androidx.core.view.forEachIndexed
import androidx.fragment.app.Fragment
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import cafe.adriel.verne.domain.model.ExplorerItem
import cafe.adriel.verne.presentation.R
import cafe.adriel.verne.presentation.extension.color
import cafe.adriel.verne.presentation.extension.colorFromAttr
import cafe.adriel.verne.presentation.extension.dpToPx
import cafe.adriel.verne.presentation.extension.drawable
import cafe.adriel.verne.presentation.extension.font
import cafe.adriel.verne.presentation.extension.fromHtml
import cafe.adriel.verne.presentation.extension.hasSpans
import cafe.adriel.verne.presentation.extension.hideAnimated
import cafe.adriel.verne.presentation.extension.hideKeyboard
import cafe.adriel.verne.presentation.extension.intentFor
import cafe.adriel.verne.presentation.extension.long
import cafe.adriel.verne.presentation.extension.openInChromeTab
import cafe.adriel.verne.presentation.extension.readText
import cafe.adriel.verne.presentation.extension.setAnimatedState
import cafe.adriel.verne.presentation.extension.setMargins
import cafe.adriel.verne.presentation.extension.showAnimated
import cafe.adriel.verne.presentation.extension.showKeyboard
import cafe.adriel.verne.presentation.ui.BaseActivity
import cafe.adriel.verne.presentation.ui.editor.typography.TypographyDialogFragment
import cafe.adriel.verne.presentation.ui.editor.typography.TypographyDialogFragmentListener
import cafe.adriel.verne.presentation.util.AnalyticsUtil
import cafe.adriel.verne.presentation.util.AndroidBug5497Workaround
import cafe.adriel.verne.presentation.util.StatefulLayoutController
import com.afollestad.materialdialogs.MaterialDialog
import com.google.android.material.snackbar.Snackbar
import com.rw.keyboardlistener.KeyboardUtils
import kotlinx.android.synthetic.main.activity_editor.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.wordpress.aztec.EnhancedMovementMethod
import org.wordpress.aztec.IHistoryListener

class EditorActivity : BaseActivity<EditorViewState>(), TypographyDialogFragmentListener {

    companion object {
        private const val EXTRA_FILE_PATH = "filePath"

        private val TOOLBAR_BUTTONS = setOf(
            R.id.format_bar_button_heading,
            R.id.format_bar_button_list,
            R.id.format_bar_button_quote,
            R.id.format_bar_button_underline,
            R.id.format_bar_button_strikethrough,
            R.id.format_bar_button_bold,
            R.id.format_bar_button_italic,
            R.id.format_bar_button_link)

        fun start(context: Context, item: ExplorerItem) {
            val intent = context.intentFor<EditorActivity>(EXTRA_FILE_PATH to item.path)
            context.startActivity(intent)
            AnalyticsUtil.logOpenFile()
        }
    }

    override val viewModel by viewModel<EditorViewModel>()

    private val actionNewFile by lazy { "$packageName.action.NEW_FILE" }

    private lateinit var menu: Menu
    private var externalText: CharSequence? = null
    private var keyboardVisible = false
    private val bug5497Workaround by lazy { AndroidBug5497Workaround(this) }
    private val stateLayoutCtrl by lazy { StatefulLayoutController.createController(this) }
    private val actionDelayMs by lazy { long(R.integer.action_delay) }

    private val saveDrawable by lazy {
        drawable(R.drawable.ic_check, color(R.color.colorAccent))
    }
    private val loadingDrawable by lazy {
        CircularProgressDrawable(this).apply {
            setColorSchemeColors(colorFromAttr(android.R.attr.actionMenuTextColor))
            centerRadius = 20f
            strokeWidth = 5f
            start()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editor)
        setSupportActionBar(vToolbar)
        vToolbar.overflowIcon?.setTint(colorFromAttr(R.attr.actionMenuTextColor))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        if (savedInstanceState == null) {
            viewModel.item = getItemFromIntent(intent)
        }

        vStateLayout.setStateController(stateLayoutCtrl)
        vEditMode.setOnClickListener { viewModel.toggleEditMode() }
        vScroll.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_UP && !vEditor.hasFocus() && viewModel.editMode) {
                vEditor.requestFocus()
                vEditor.showKeyboard()
            }
            false
        }
        vEditor.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus && viewModel.editMode) {
                vEditorToolbar.showAnimated()
            } else if (keyboardVisible || vTitle.hasFocus()) {
                vEditorToolbar.visibility = View.GONE
            } else {
                vEditorToolbar.hideAnimated()
            }
        }

        KeyboardUtils.addKeyboardToggleListener(this) { keyboardVisible = it }

        setupEditor()
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        stateLayoutCtrl.setAnimatedState(vStateLayout, StatefulLayoutController.STATE_PROGRESS)
        launch { loadText() }
    }

    override fun onBackPressed() {
        if (viewModel.editMode) {
            launch { saveText() }
        } else {
            super.onBackPressed()
        }
    }

    override fun onResume() {
        super.onResume()
        bug5497Workaround.addListener()
        viewModel.requestStateUpdate()
    }

    override fun onPause() {
        bug5497Workaround.removeListener()
        launch { saveText(true) }
        super.onPause()
    }

    override fun onDestroy() {
        KeyboardUtils.removeAllKeyboardToggleListeners()
        vEditor.setOnUrlClickListener(null)
        super.onDestroy()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        viewModel.requestStateUpdate()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.editor, menu)
        this.menu = menu
        setUndoMenuItemEnabled(false)
        setRedoMenuItemEnabled(false)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?) = when (item?.itemId) {
        android.R.id.home -> {
            if (viewModel.editMode) {
                launch { saveText() }
            } else {
                NavUtils.navigateUpFromSameTask(this)
            }
            true
        }
        R.id.action_undo -> {
            vEditor.undo()
            true
        }
        R.id.action_redo -> {
            vEditor.redo()
            true
        }
        R.id.action_typography -> {
            showTypographyDialog()
            true
        }
        R.id.action_statistics -> {
            showStatisticsDialog()
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    override fun onAttachFragment(fragment: Fragment) {
        when (fragment) {
            is TypographyDialogFragment -> fragment.listener = this
        }
    }

    override fun onSettingsChanged() {
        viewModel.onSettingsChanged()
    }

    override fun onStateUpdated(state: EditorViewState) {
        state.apply {
            vTitle.setText(viewModel.item.title)

            setEditModeEnabled(editMode)
            setUndoMenuItemEnabled(vEditor.history.undoValid())
            setRedoMenuItemEnabled(vEditor.history.redoValid())

            with(settings) {
                font(fontFamily.resId) {
                    vTitle.typeface = it
                    vEditor.typeface = it
                }

                val padding = marginSize.dpToPx()

                vTitle.textSize = fontSize + 4f
                vTitle.setPaddingRelative(padding, 0, padding, 0)

                vEditor.textSize = fontSize.toFloat()
                vEditor.setPaddingRelative(padding, 0, padding, 0)
            }
        }
    }

    private fun setupEditor() {
        vEditor.setToolbar(vEditorToolbar)
        vEditor.isInCalypsoMode = false
        vEditor.history.setHistoryListener(object : IHistoryListener {
            override fun onUndoEnabled() {
                setUndoMenuItemEnabled(vEditor.history.undoValid())
            }
            override fun onRedoEnabled() {
                setRedoMenuItemEnabled(vEditor.history.redoValid())
            }
        })
        vEditor.setOnUrlClickListener(object : EnhancedMovementMethod.OnUrlClickListener {
            override fun onClick(widget: View, url: String) {
                Uri.parse(url).openInChromeTab(this@EditorActivity)
            }
        })

        vEditorToolbar.setEditor(vEditor, null)
        vEditorToolbar.findViewById<View>(R.id.format_bar_horizontal_divider).apply {
            setBackgroundColor(color(R.color.colorPrimaryDark))
        }
        vEditorToolbar.findViewById<HorizontalScrollView>(R.id.format_bar_button_scroll).apply {
            setBackgroundColor(color(R.color.colorPrimaryDark))
            isHorizontalScrollBarEnabled = false
        }
        vEditorToolbar.findViewById<LinearLayout>(R.id.format_bar_button_layout_expanded).apply {
            var linkButtonView: View? = null
            var linkButtonIndex = -1
            forEachIndexed { index, view ->
                view.visibility = if (view.id in TOOLBAR_BUTTONS) View.VISIBLE else View.GONE
                if (view.id == R.id.format_bar_button_link) {
                    linkButtonView = view
                    linkButtonIndex = index
                }
            }
            // Move Link Button to a new position
            if (linkButtonView != null && linkButtonIndex >= 0) {
                removeViewAt(linkButtonIndex)
                addView(linkButtonView, 2)
            }
        }
    }

    private fun getItemFromIntent(intent: Intent): ExplorerItem = when {
        intent.hasExtra(EXTRA_FILE_PATH) -> {
            val filePath = intent.getStringExtra(EXTRA_FILE_PATH)
            ExplorerItem.File(filePath)
        }
        intent.action == actionNewFile -> {
            AnalyticsUtil.logNewFile(AnalyticsUtil.NEW_FILE_SOURCE_SHORTCUT)
            viewModel.createEmptyFileItem()
        }
        intent.action == Intent.ACTION_SEND -> {
            AnalyticsUtil.logNewFile(AnalyticsUtil.NEW_FILE_SOURCE_EXTERNAL)
            val title = when {
                intent.hasExtra(Intent.EXTRA_SUBJECT) -> intent.getStringExtra(Intent.EXTRA_SUBJECT)
                intent.hasExtra(Intent.EXTRA_TITLE) -> intent.getStringExtra(Intent.EXTRA_TITLE)
                else -> null
            }
            externalText = when {
                intent.hasExtra(Intent.EXTRA_HTML_TEXT) -> intent.getCharSequenceExtra(Intent.EXTRA_HTML_TEXT)
                intent.hasExtra(Intent.EXTRA_TEXT) -> intent.getCharSequenceExtra(Intent.EXTRA_TEXT)
                intent.hasExtra(Intent.EXTRA_STREAM) -> {
                    val uri = intent.getParcelableExtra<Uri?>(Intent.EXTRA_STREAM)
                    if(uri?.path?.endsWith(".html") == true) {
                        uri.readText(this)
                    } else {
                        Snackbar.make(vRoot, R.string.unsupported_file_format, Snackbar.LENGTH_SHORT).show()
                        null
                    }
                }
                intent.data != null -> intent.data?.readText(this)
                else -> null
            }
            viewModel.createEmptyFileItem(title)
        }
        else -> viewModel.createEmptyFileItem()
    }

    private suspend fun loadText() {
        if (externalText != null) {
            if (externalText?.hasSpans() == true) {
                vEditor.setText(externalText)
            } else {
                val html = externalText?.toString()?.replace("\n", "<br>")
                vEditor.fromHtml(html ?: "")
            }
            externalText = null
        } else {
            val text = viewModel.getHtmlText()
            vEditor.fromHtml(text)
        }
        stateLayoutCtrl.setAnimatedState(vStateLayout, StatefulLayoutController.STATE_CONTENT)
    }

    private suspend fun saveText(silent: Boolean = false) {
        if (!silent) {
            supportActionBar?.setHomeAsUpIndicator(loadingDrawable)
            vEditor.hideKeyboard()
            delay(actionDelayMs)
        }

        withContext(Dispatchers.Default) {
            val title = vTitle.text.toString()
            val text = vEditor.toHtml()
            viewModel.saveText(title, text)
        }

        if (!silent) {
            viewModel.toggleEditMode()
        }
    }

    private fun setUndoMenuItemEnabled(enabled: Boolean) {
        if (::menu.isInitialized) {
            menu.findItem(R.id.action_undo).isEnabled = enabled
        }
    }

    private fun setRedoMenuItemEnabled(enabled: Boolean) {
        if (::menu.isInitialized) {
            menu.findItem(R.id.action_redo).isEnabled = enabled
        }
    }

    private fun setEditModeEnabled(enabled: Boolean) {
        vTitle.isCursorVisible = enabled
        vTitle.isFocusable = enabled
        vTitle.isFocusableInTouchMode = enabled

        vEditor.isCursorVisible = enabled
        vEditor.isFocusable = enabled
        vEditor.isFocusableInTouchMode = enabled
        vEditor.setUrlClickable(!enabled)

        if (enabled) {
            supportActionBar?.setHomeAsUpIndicator(saveDrawable)
            vScroll.setMargins(bottom = resources.getDimensionPixelSize(R.dimen.aztec_format_bar_height))
            vEditMode.hide()
        } else {
            supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_arrow_back)
            vScroll.setMargins()
            vEditMode.show()
        }
    }

    private fun showTypographyDialog() {
        TypographyDialogFragment.show(supportFragmentManager)
    }

    private fun showStatisticsDialog() {
        vEditor.text?.toString()?.let { text ->
            launch {
                val message = viewModel.getStatisticsText(text)
                MaterialDialog(this@EditorActivity).show {
                    title(R.string.statistics)
                    message(text = message.fromHtml(), html = true)
                    positiveButton(android.R.string.ok)
                }
                AnalyticsUtil.logShowStatistics()
            }
        }
    }
}
