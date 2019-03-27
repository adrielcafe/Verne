package cafe.adriel.verne.presentation.ui

import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatDelegate
import cafe.adriel.androidcoroutinescopes.appcompat.CoroutineScopedActivity
import cafe.adriel.verne.presentation.R
import cafe.adriel.verne.presentation.extension.isDarkMode
import cafe.adriel.verne.presentation.extension.isFullScreen
import com.etiennelenhart.eiffel.state.ViewState
import com.etiennelenhart.eiffel.viewmodel.StateViewModel

abstract class BaseActivity<S : ViewState> : CoroutineScopedActivity() {

    protected abstract val viewModel: StateViewModel<S>

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)

        val darkMode = if (isDarkMode()) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
        AppCompatDelegate.setDefaultNightMode(darkMode)

        if (isFullScreen()) {
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

    protected abstract fun onStateUpdated(state: S)
}
