package cafe.adriel.verne.presentation.helper

import android.app.Activity
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatDelegate
import cafe.adriel.verne.presentation.R

class ThemeHelper(private val preferencesHelper: PreferencesHelper) {

    /**
     * Must be called before `super.onCreate()`
     */
    fun init(activity: Activity) {
        activity.apply {
            setTheme(R.style.AppTheme)

            updateDarkMode(preferencesHelper.isDarkMode())
            updateFullscreen(window, preferencesHelper.isFullscreen())
        }
    }

    private fun updateDarkMode(isDarkMode: Boolean) {
        val nightMode = if (isDarkMode) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
        AppCompatDelegate.setDefaultNightMode(nightMode)
    }

    private fun updateFullscreen(window: Window, isFullscreen: Boolean) {
        if (isFullscreen) {
            window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        } else {
            window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        }
    }
}
