package cafe.adriel.verne.view

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatDelegate
import cafe.adriel.androidcoroutinescopes.appcompat.CoroutineScopedActivity
import cafe.adriel.verne.R
import cafe.adriel.verne.extension.isDarkMode
import cafe.adriel.verne.extension.isFullscreen
import com.etiennelenhart.eiffel.state.ViewState
import com.etiennelenhart.eiffel.viewmodel.StateViewModel
import com.franmontiel.localechanger.LocaleChanger

abstract class BaseActivity<S : ViewState> : CoroutineScopedActivity() {

    // LocaleChanger overrides the original context, but we need it to print files
    protected var originalContext: Context? = null
    protected abstract val viewModel: StateViewModel<S>

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)

        val darkMode = if (isDarkMode()) {
            AppCompatDelegate.MODE_NIGHT_YES
        } else {
            AppCompatDelegate.MODE_NIGHT_NO
        }
        AppCompatDelegate.setDefaultNightMode(darkMode)

        if (isFullscreen()) {
            window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        } else {
            window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        }

        super.onCreate(savedInstanceState)
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        viewModel.observeState(this, ::onStateUpdated)
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        LocaleChanger.onConfigurationChanged()
    }

    override fun attachBaseContext(newBase: Context?) {
        originalContext = newBase
        super.attachBaseContext(LocaleChanger.configureBaseContext(newBase))
    }

    protected abstract fun onStateUpdated(state: S)
}
