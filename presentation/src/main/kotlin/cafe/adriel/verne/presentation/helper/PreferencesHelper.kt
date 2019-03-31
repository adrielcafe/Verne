package cafe.adriel.verne.presentation.helper

import android.content.SharedPreferences
import androidx.core.content.edit

class PreferencesHelper(private val preferences: SharedPreferences) {

    companion object {
        const val PREF_DARK_MODE = "appDarkMode"
        const val PREF_FULLSCREEN = "appFullscreen"
        const val PREF_FONT_FAMILY = "editorFontFamily"
        const val PREF_FONT_SIZE = "editorFontSize"
        const val PREF_MARGIN_SIZE = "editorMarginSize"

        const val DEFAULT_DARK_MODE = false
        const val DEFAULT_FULLSCREEN = false
        const val DEFAULT_FONT_FAMILY = "ALICE"
        const val DEFAULT_FONT_SIZE = 18
        const val DEFAULT_MARGIN_SIZE = 20
    }

    fun isDarkMode() = preferences.getBoolean(PREF_DARK_MODE, DEFAULT_DARK_MODE)

    fun isFullscreen() = preferences.getBoolean(PREF_FULLSCREEN, DEFAULT_FULLSCREEN)

    fun getFontFamily() =
        preferences.getString(PREF_FONT_FAMILY, DEFAULT_FONT_FAMILY)?.toUpperCase() ?: DEFAULT_FONT_FAMILY

    fun setFontFamily(newValue: String) = preferences.edit { putString(PREF_FONT_FAMILY, newValue.toUpperCase()) }

    fun getFontSize() = preferences.getInt(PREF_FONT_SIZE, DEFAULT_FONT_SIZE)

    fun setFontSize(newValue: Int) = preferences.edit { putInt(PREF_FONT_SIZE, newValue) }

    fun getMarginSize() = preferences.getInt(PREF_MARGIN_SIZE, DEFAULT_MARGIN_SIZE)

    fun setMarginSize(newValue: Int) = preferences.edit { putInt(PREF_MARGIN_SIZE, newValue) }
}
