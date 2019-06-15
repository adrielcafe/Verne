package cafe.adriel.verne.presentation.ui.editor

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import cafe.adriel.verne.domain.interactor.explorer.ItemTextExplorerInteractor
import cafe.adriel.verne.domain.interactor.explorer.RenameItemExplorerInteractor
import cafe.adriel.verne.domain.interactor.settings.FontFamilySettingsInteractor
import cafe.adriel.verne.domain.interactor.settings.FontSizeSettingsInteractor
import cafe.adriel.verne.domain.interactor.settings.MarginSizeSettingsInteractor
import cafe.adriel.verne.domain.model.ExplorerItem
import cafe.adriel.verne.presentation.R
import cafe.adriel.verne.presentation.extension.charCount
import cafe.adriel.verne.presentation.extension.formatSeconds
import cafe.adriel.verne.presentation.extension.paragraphCount
import cafe.adriel.verne.presentation.extension.readTimeInSeconds
import cafe.adriel.verne.presentation.extension.wordCount
import cafe.adriel.verne.presentation.model.FontFamily
import cafe.adriel.verne.presentation.model.TypographySettings
import cafe.adriel.verne.shared.extension.formatShort
import cafe.adriel.verne.shared.extension.withIo
import cafe.adriel.verne.shared.model.AppConfig
import com.etiennelenhart.eiffel.viewmodel.StateViewModel
import kotlinx.coroutines.launch
import java.io.File
import java.util.Calendar

class EditorViewModel(
    private val appContext: Context,
    private val appConfig: AppConfig,
    private val fontFamilyInteractor: FontFamilySettingsInteractor,
    private val fontSizeInteractor: FontSizeSettingsInteractor,
    private val marginSizeInteractor: MarginSizeSettingsInteractor,
    private val renameItemInteractor: RenameItemExplorerInteractor,
    private val itemTextInteractor: ItemTextExplorerInteractor
) : StateViewModel<EditorViewState>() {

    override val state = MutableLiveData<EditorViewState>()

    lateinit var item: ExplorerItem.File
    var editMode = false
        private set

    init {
        initState { EditorViewState() }
        onSettingsChanged()
    }

    fun onSettingsChanged() {
        viewModelScope.launch {
            updateState { it.copy(settings = getSettings()) }
        }
    }

    fun requestStateUpdate() = updateState { it }

    fun toggleEditMode() {
        editMode = !editMode
        updateState { it.copy(editMode = editMode) }
    }

    fun createEmptyFileItem(name: String? = null): ExplorerItem.File {
        val fileName = name ?: getDefaultFileName()
        val file = File(appConfig.explorerRootFolder.path, fileName)
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
        } catch (e: Exception) {
            e.printStackTrace()
            ""
        }
    }

    suspend fun getStatisticsText(text: String): String {
        val paragraphCount = text.paragraphCount()
        val wordCount = text.wordCount()
        val charCountWithWhitespace = text.length
        val charCountWithoutWhitespace = text.charCount()
        val readTime = text.readTimeInSeconds().formatSeconds(appContext)

        return appContext.run {
            """
            <b>${getString(R.string.paragraphs)}</b>
            $paragraphCount

            <b>${getString(R.string.words)}</b>
            $wordCount

            <b>${getString(R.string.chars)} (${getString(R.string.without_space).decapitalize()})</b>
            $charCountWithoutWhitespace

            <b>${getString(R.string.chars)} (${getString(R.string.with_space).decapitalize()})</b>
            $charCountWithWhitespace

            <b>${getString(R.string.read_time)}</b>
            $readTime
        """.trimIndent().replace("\n", "<br>")
        }
    }

    private suspend fun getSettings() =
        TypographySettings(
            FontFamily.valueOf(fontFamilyInteractor.get()),
            fontSizeInteractor.get(),
            marginSizeInteractor.get()
        )

    private fun getDefaultFileName() =
        Calendar.getInstance()
            .time
            .formatShort()
            .replace('/', '-')
}
