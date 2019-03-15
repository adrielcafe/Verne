package cafe.adriel.verne.view.editor

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.MutableLiveData
import cafe.adriel.verne.R
import cafe.adriel.verne.extension.charCount
import cafe.adriel.verne.extension.formatSeconds
import cafe.adriel.verne.extension.formatShort
import cafe.adriel.verne.extension.paragraphCount
import cafe.adriel.verne.extension.readTimeInSeconds
import cafe.adriel.verne.extension.wordCount
import cafe.adriel.verne.model.ExplorerItem
import cafe.adriel.verne.model.FontFamily
import cafe.adriel.verne.model.TypographySettings
import cafe.adriel.verne.repository.LocalExplorerRepository
import cafe.adriel.verne.util.CoroutineScopedStateViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.util.Calendar

class EditorViewModel(private val appContext: Context, private val explorerRepository: LocalExplorerRepository, private val preferences: SharedPreferences) :
    CoroutineScopedStateViewModel<EditorViewState>(), SharedPreferences.OnSharedPreferenceChangeListener {

    override val state = MutableLiveData<EditorViewState>()

    lateinit var item: ExplorerItem
    var editMode = false
        set(value) {
            field = value
            launch {
                updateState { it.copy(editMode = editMode) }
            }
        }

    init {
        preferences.registerOnSharedPreferenceChangeListener(this)
        initState { EditorViewState(false, getSettings()) }
    }

    override fun onCleared() {
        super.onCleared()
        preferences.unregisterOnSharedPreferenceChangeListener(this)
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, key: String) {
        if (key in arrayOf(TypographySettings.TYPOGRAPHY_FONT_FAMILY, TypographySettings.TYPOGRAPHY_FONT_SIZE, TypographySettings.TYPOGRAPHY_MARGIN_SIZE)) {
            updateState { it.copy(settings = getSettings()) }
        }
    }

    fun requestStateUpdate() = updateState { it }

    fun toggleEditMode() {
        editMode = !editMode
    }

    fun createEmptyFileItem(name: String? = null): ExplorerItem {
        val fileName = name ?: Calendar.getInstance().time.formatShort().replace('/', '-')
        val file = File(explorerRepository.baseDir, fileName)
        return ExplorerItem.File(file.path)
    }

    suspend fun saveText(title: String, text: String) = withContext(Dispatchers.IO) {
        try {
            // Rename the file if the title has changed
            if (title != item.title) {
                val newFile = explorerRepository.rename(item, title)
                item = ExplorerItem.File(newFile.path)
            }
            explorerRepository.create(item)
            item.file.writeText(text)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun getHtmlText() = withContext(Dispatchers.IO) {
        try {
            explorerRepository.getHtmlText(item)
                .also { if (it.isBlank()) editMode = true }
        } catch (e: Exception) {
            e.printStackTrace()
            editMode = true
            ""
        }
    }

    suspend fun getTextStatistics(text: String): String {
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

    private fun getSettings(): TypographySettings {
        val fontFamilyValue = preferences.getString(TypographySettings.TYPOGRAPHY_FONT_FAMILY, TypographySettings.DEFAULT_FONT_FAMILY)
        val fontFamily = try {
            FontFamily.valueOf(fontFamilyValue!!)
        } catch (e: java.lang.Exception) {
            FontFamily.valueOf(TypographySettings.DEFAULT_FONT_FAMILY)
        }
        val fontSize = preferences.getInt(TypographySettings.TYPOGRAPHY_FONT_SIZE, TypographySettings.DEFAULT_FONT_SIZE)
        val marginSize = preferences.getInt(TypographySettings.TYPOGRAPHY_MARGIN_SIZE, TypographySettings.DEFAULT_MARGIN_SIZE)
        return TypographySettings(fontFamily, fontSize, marginSize)
    }
}
