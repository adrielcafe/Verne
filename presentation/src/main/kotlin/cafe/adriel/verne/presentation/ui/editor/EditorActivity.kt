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
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NavUtils
import androidx.core.view.forEachIndexed
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
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
import cafe.adriel.verne.presentation.extension.int
import cafe.adriel.verne.presentation.extension.intent
import cafe.adriel.verne.presentation.extension.long
import cafe.adriel.verne.presentation.extension.openInChromeTab
import cafe.adriel.verne.presentation.extension.readText
import cafe.adriel.verne.presentation.extension.setMargins
import cafe.adriel.verne.presentation.extension.setStateWithAnimation
import cafe.adriel.verne.presentation.extension.showAnimated
import cafe.adriel.verne.presentation.extension.showKeyboard
import cafe.adriel.verne.presentation.extension.showSnackBar
import cafe.adriel.verne.presentation.helper.AnalyticsHelper
import cafe.adriel.verne.presentation.helper.CustomTabsHelper
import cafe.adriel.verne.presentation.helper.FullscreenKeyboardHelper
import cafe.adriel.verne.presentation.helper.StatefulLayoutHelper
import cafe.adriel.verne.presentation.helper.ThemeHelper
import cafe.adriel.verne.presentation.ui.editor.typography.TypographyFragment
import cafe.adriel.verne.presentation.ui.editor.typography.TypographyFragmentListener
import cafe.adriel.verne.presentation.util.StateAware
import cafe.adriel.verne.shared.extension.withDefault
import com.afollestad.materialdialogs.MaterialDialog
import com.rw.keyboardlistener.KeyboardUtils
import kotlinx.android.synthetic.main.activity_editor.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.wordpress.aztec.AztecText
import org.wordpress.aztec.IHistoryListener

class EditorActivity : AppCompatActivity(), StateAware<EditorViewState>, TypographyFragmentListener {

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
            R.id.format_bar_button_link
        )

        fun start(context: Context, item: ExplorerItem.File) {
            val intent = context.intent<EditorActivity>().putExtra(EXTRA_FILE_PATH, item.path)
            context.startActivity(intent)
        }
    }

    override val viewModel by viewModel<EditorViewModel>()
    private val analyticsHelper by inject<AnalyticsHelper>()
    private val themeHelper by inject<ThemeHelper>()
    private val customTabsHelper by inject<CustomTabsHelper>()
    private val fullscreenKeyboardHelper by inject<FullscreenKeyboardHelper>()
    private val statefulLayoutHelper by inject<StatefulLayoutHelper>()

    private val actionNewFile by lazy { "$packageName.action.NEW_FILE" }

    private var menu: Menu? = null
    private var externalText: CharSequence? = null
    private var keyboardVisible = false
    private val actionDelayMs by lazy { long(R.integer.action_delay) }

    private val saveDrawable by lazy {
        drawable(R.drawable.ic_check, color(R.color.colorAccent))
    }
    private val loadingDrawable by lazy {
        CircularProgressDrawable(this).apply {
            setColorSchemeColors(colorFromAttr(android.R.attr.actionMenuTextColor))
            centerRadius = int(R.integer.loading_drawable_center_radius).toFloat()
            strokeWidth = int(R.integer.loading_drawable_stroke_width).toFloat()
            start()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        themeHelper.init(this)

        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_editor)
        setSupportActionBar(vToolbar)
        vToolbar.overflowIcon?.setTint(colorFromAttr(R.attr.actionMenuTextColor))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        statefulLayoutHelper.init(this)
        fullscreenKeyboardHelper.init(this)

        if (savedInstanceState == null) viewModel.item = getItemFromIntent(intent)

        initViews()
        initEditor()
        initEditorToolbar()
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        viewModel.observeState(this, ::onStateUpdated)
        statefulLayoutHelper.controller.setStateWithAnimation(vStateLayout, StatefulLayoutHelper.STATE_PROGRESS)
        loadText()
    }

    override fun onBackPressed() {
        if (viewModel.editMode) {
            saveText()
        } else {
            super.onBackPressed()
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.requestStateUpdate()
    }

    override fun onPause() {
        saveText(true)
        super.onPause()
    }

    override fun onDestroy() {
        KeyboardUtils.removeAllKeyboardToggleListeners()
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
                saveText()
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
            is TypographyFragment -> fragment.listener = this
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
                lifecycleScope.launch {
                    font(fontFamily.resId)?.let { typeface ->
                        vTitle.typeface = typeface
                        vEditor.typeface = typeface
                    }
                }

                val padding = marginSize.dpToPx()

                vTitle.textSize = fontSize + int(R.integer.title_additional_font_size).toFloat()
                vTitle.setPaddingRelative(padding, 0, padding, 0)

                vEditor.textSize = fontSize.toFloat()
                vEditor.setPaddingRelative(padding, 0, padding, 0)
            }
        }
    }

    private fun initViews() {
        vStateLayout.setStateController(statefulLayoutHelper.controller)
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
    }

    private fun initEditor() {
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
        vEditor.setOnLinkTappedListener(object : AztecText.OnLinkTappedListener {
            override fun onLinkTapped(widget: View, url: String) {
                Uri.parse(url).openInChromeTab(this@EditorActivity, customTabsHelper.packageNameToUse)
            }
        })
    }

    private fun initEditorToolbar() {
        vEditorToolbar.setEditor(vEditor, null)
        vEditorToolbar.findViewById<View>(R.id.format_bar_horizontal_divider)
            .setBackgroundColor(color(R.color.colorPrimaryDark))
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

    private fun getItemFromIntent(intent: Intent): ExplorerItem.File = when {
        intent.hasExtra(EXTRA_FILE_PATH) -> {
            analyticsHelper.logOpenFile()
            val filePath = intent.getStringExtra(EXTRA_FILE_PATH)
            ExplorerItem.File(filePath)
        }
        intent.action == actionNewFile -> {
            analyticsHelper.logNewFile(AnalyticsHelper.NEW_FILE_SOURCE_SHORTCUT)
            viewModel.createEmptyFileItem()
        }
        intent.action == Intent.ACTION_SEND -> {
            analyticsHelper.logNewFile(AnalyticsHelper.NEW_FILE_SOURCE_EXTERNAL)
            val title = when {
                intent.hasExtra(Intent.EXTRA_SUBJECT) -> intent.getStringExtra(Intent.EXTRA_SUBJECT)
                intent.hasExtra(Intent.EXTRA_TITLE) -> intent.getStringExtra(Intent.EXTRA_TITLE)
                else -> null
            }
            externalText = getExternalTextFromIntent(intent)
            viewModel.createEmptyFileItem(title)
        }
        else -> viewModel.createEmptyFileItem()
    }

    private fun getExternalTextFromIntent(intent: Intent) = when {
        intent.hasExtra(Intent.EXTRA_HTML_TEXT) -> intent.getCharSequenceExtra(Intent.EXTRA_HTML_TEXT)
        intent.hasExtra(Intent.EXTRA_TEXT) -> intent.getCharSequenceExtra(Intent.EXTRA_TEXT)
        intent.hasExtra(Intent.EXTRA_STREAM) -> {
            val uri = intent.getParcelableExtra<Uri?>(Intent.EXTRA_STREAM)
            if (uri?.path?.endsWith(".html") == true) {
                uri.readText(this)
            } else {
                showSnackBar(R.string.unsupported_file_format)
                null
            }
        }
        intent.data != null -> intent.data?.readText(this)
        else -> null
    }

    private fun loadText() {
        lifecycleScope.launch {
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
            statefulLayoutHelper.controller.setStateWithAnimation(vStateLayout, StatefulLayoutHelper.STATE_CONTENT)
        }
    }

    private fun saveText(silent: Boolean = false) {
        lifecycleScope.launch {
            if (!silent) {
                supportActionBar?.setHomeAsUpIndicator(loadingDrawable)
                vEditor.hideKeyboard()
                delay(actionDelayMs)
            }

            withDefault {
                val title = vTitle.text.toString()
                val text = vEditor.toHtml()
                viewModel.saveText(title, text)
            }

            if (!silent) {
                viewModel.toggleEditMode()
            }
        }
    }

    private fun setUndoMenuItemEnabled(enabled: Boolean) {
        menu?.findItem(R.id.action_undo)?.isEnabled = enabled
    }

    private fun setRedoMenuItemEnabled(enabled: Boolean) {
        menu?.findItem(R.id.action_redo)?.isEnabled = enabled
    }

    private fun setEditModeEnabled(enabled: Boolean) {
        vTitle.isCursorVisible = enabled
        vTitle.isFocusable = enabled
        vTitle.isFocusableInTouchMode = enabled

        vEditor.isCursorVisible = enabled
        vEditor.isFocusable = enabled
        vEditor.isFocusableInTouchMode = enabled
        vEditor.setLinkTapEnabled(!enabled)

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
        TypographyFragment.show(supportFragmentManager)
    }

    private fun showStatisticsDialog() {
        vEditor.text?.toString()?.let { text ->
            lifecycleScope.launch {
                val message = viewModel.getStatisticsHtmlText(text).fromHtml()
                MaterialDialog(this@EditorActivity).show {
                    title(R.string.statistics)
                    message(text = message, html = true)
                    positiveButton(android.R.string.ok)
                }
                analyticsHelper.logShowStatistics()
            }
        }
    }
}
