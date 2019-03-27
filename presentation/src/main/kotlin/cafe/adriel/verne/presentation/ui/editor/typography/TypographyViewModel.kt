package cafe.adriel.verne.presentation.ui.editor.typography

import android.content.SharedPreferences
import androidx.core.content.edit
import cafe.adriel.androidcoroutinescopes.viewmodel.CoroutineScopedViewModel
import cafe.adriel.verne.presentation.model.FontFamily
import cafe.adriel.verne.presentation.model.TypographySettings

class TypographyViewModel(private val preferences: SharedPreferences) : CoroutineScopedViewModel() {

    var fontFamily: FontFamily
        get() {
            val value = preferences.getString(TypographySettings.TYPOGRAPHY_FONT_FAMILY, null)
            return try {
                FontFamily.valueOf(value!!)
            } catch (e: Exception) {
                FontFamily.valueOf(TypographySettings.DEFAULT_FONT_FAMILY)
            }
        }
        set(value) = preferences.edit { putString(TypographySettings.TYPOGRAPHY_FONT_FAMILY, value.name) }

    var fontSize: Int
        get() = preferences.getInt(TypographySettings.TYPOGRAPHY_FONT_SIZE, TypographySettings.DEFAULT_FONT_SIZE)
        set(value) = preferences.edit { putInt(TypographySettings.TYPOGRAPHY_FONT_SIZE, value) }

    var marginSize: Int
        get() = preferences.getInt(TypographySettings.TYPOGRAPHY_MARGIN_SIZE, TypographySettings.DEFAULT_MARGIN_SIZE)
        set(value) = preferences.edit { putInt(TypographySettings.TYPOGRAPHY_MARGIN_SIZE, value) }
}
