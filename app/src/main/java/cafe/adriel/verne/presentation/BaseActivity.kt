package cafe.adriel.verne.presentation

import android.content.Context
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
import com.franmontiel.localechanger.utils.ActivityRecreationHelper

abstract class BaseActivity<S : ViewState> : CoroutineScopedActivity() {

    protected abstract val viewModel: StateViewModel<S>

    // LocaleChanger overrides the original context, but we need it to print files
    protected lateinit var originalContext: Context

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)

        val darkMode = if (isDarkMode()) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
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

    override fun onResume() {
        super.onResume()
        ActivityRecreationHelper.onResume(this)
    }

    override fun onDestroy() {
        ActivityRecreationHelper.onDestroy(this)
        super.onDestroy()
    }

    override fun attachBaseContext(newBase: Context) {
        originalContext = newBase
        super.attachBaseContext(LocaleChanger.configureBaseContext(newBase))
    }

    protected abstract fun onStateUpdated(state: S)
}
