package cafe.adriel.verne.presentation.helper

import android.content.SharedPreferences

class PreferencesHelper(private val preferences: SharedPreferences) {

    companion object {
        const val PREF_DARK_MODE = "appDarkMode"
        const val PREF_FULLSCREEN = "appFullscreen"

        const val DEFAULT_DARK_MODE = false
        const val DEFAULT_FULLSCREEN = false
    }

    fun isDarkMode() = preferences.getBoolean(PREF_DARK_MODE, DEFAULT_DARK_MODE)

    fun isFullscreen() = preferences.getBoolean(PREF_FULLSCREEN, DEFAULT_FULLSCREEN)
}
