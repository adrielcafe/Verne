package cafe.adriel.verne.presentation.ui

import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatDelegate
import cafe.adriel.androidcoroutinescopes.appcompat.CoroutineScopedActivity
import cafe.adriel.verne.presentation.R
import cafe.adriel.verne.presentation.helper.PreferencesHelper
import org.koin.android.ext.android.inject

abstract class BaseActivity : CoroutineScopedActivity() {

    protected val preferencesHelper by inject<PreferencesHelper>()

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)

        val darkMode = if (preferencesHelper.isDarkMode()) {
            AppCompatDelegate.MODE_NIGHT_YES
        } else {
            AppCompatDelegate.MODE_NIGHT_NO
        }
        AppCompatDelegate.setDefaultNightMode(darkMode)

        if (preferencesHelper.isFullscreen()) {
            window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        } else {
            window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        }

        super.onCreate(savedInstanceState)
    }
}
