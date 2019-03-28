package cafe.adriel.verne.presentation.ui.editor

import android.content.Context
import androidx.lifecycle.MutableLiveData
import cafe.adriel.verne.domain.model.BaseDir
import cafe.adriel.verne.domain.model.ExplorerItem
import cafe.adriel.verne.interactor.explorer.ItemTextExplorerInteractor
import cafe.adriel.verne.interactor.explorer.RenameItemExplorerInteractor
import cafe.adriel.verne.interactor.preference.FontFamilyPreferenceInteractor
import cafe.adriel.verne.interactor.preference.FontSizePreferenceInteractor
import cafe.adriel.verne.interactor.preference.MarginSizePreferenceInteractor
import cafe.adriel.verne.presentation.R
import cafe.adriel.verne.presentation.extension.charCount
import cafe.adriel.verne.presentation.extension.formatSeconds
import cafe.adriel.verne.presentation.extension.paragraphCount
import cafe.adriel.verne.presentation.extension.readTimeInSeconds
import cafe.adriel.verne.presentation.extension.wordCount
import cafe.adriel.verne.presentation.model.FontFamily
import cafe.adriel.verne.presentation.model.TypographySettings
import cafe.adriel.verne.presentation.util.CoroutineScopedStateViewModel
import cafe.adriel.verne.shared.extension.formatShort
import cafe.adriel.verne.shared.extension.withIo
import kotlinx.coroutines.launch
import java.io.File
import java.util.Calendar

class EditorViewModel(
    private val appContext: Context,
    private val baseDir: BaseDir,
    private val renameItemInteractor: RenameItemExplorerInteractor,
    private val itemTextInteractor: ItemTextExplorerInteractor,
    private val fontFamily: FontFamilyPreferenceInteractor,
    private val fontSize: FontSizePreferenceInteractor,
    private val marginSize: MarginSizePreferenceInteractor
) : CoroutineScopedStateViewModel<EditorViewState>() {

    override val state = MutableLiveData<EditorViewState>()

    lateinit var item: ExplorerItem.File
    var editMode = false
        set(value) {
            field = value
            launch {
                updateState { it.copy(editMode = editMode) }
            }
        }

    init {
        initState { EditorViewState() }
        onSettingsChanged()
    }

    fun onSettingsChanged() {
        launch {
            updateState { it.copy(settings = getSettings()) }
        }
    }

    fun requestStateUpdate() = updateState { it }

    fun toggleEditMode() {
        editMode = !editMode
    }

    fun createEmptyFileItem(name: String? = null): ExplorerItem.File {
        val fileName = name ?: getDefaultFileName()
        val file = File(baseDir.path, fileName)
        return ExplorerItem.File(file.path)
    }

    suspend fun saveText(title: String, text: String) = withIo {
        try {
            // Rename the file if the title has changed
            if (title != item.title) {
                item = renameItemInteractor(item, title) as ExplorerItem.File
            }
            itemTextInteractor.set(item, text)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun getHtmlText() = withIo {
        try {
            itemTextInteractor.get(item)
                .also { if (it.isBlank()) editMode = true }
        } catch (e: Exception) {
            e.printStackTrace()
            editMode = true
            ""
        }
    }

    suspend fun getStatisticsText(text: String): String {
        val paragraphCount = text.paragraphCount()
        val wordCount = text.wordCount()
        val charCountWithWhitespace = text.length
        val charCountWithoutWhitespace = text.charCount()
        val readTime = text.readTimeInSeconds().formatSeconds(appContext)

        return """
            <b>${appContext.getString(R.string.paragraphs)}</b>
            $paragraphCount

            <b>${appContext.getString(R.string.words)}</b>
            $wordCount

            <b>${appContext.getString(R.string.chars)} (${appContext.getString(R.string.without_space).decapitalize()})</b>
            $charCountWithoutWhitespace

            <b>${appContext.getString(R.string.chars)} (${appContext.getString(R.string.with_space).decapitalize()})</b>
            $charCountWithWhitespace

            <b>${appContext.getString(R.string.read_time)}</b>
            $readTime
        """.trimIndent().replace("\n", "<br>")
    }

    private suspend fun getSettings() =
        TypographySettings(FontFamily.valueOf(fontFamily.get()), fontSize.get(), marginSize.get())

    private fun getDefaultFileName() =
        Calendar.getInstance()
            .time
            .formatShort()
            .replace('/', '-')
}
