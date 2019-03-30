package cafe.adriel.verne.presentation.di

import android.content.Context
import cafe.adriel.verne.presentation.helper.AnalyticsHelper
import cafe.adriel.verne.presentation.helper.CustomTabsHelper
import cafe.adriel.verne.presentation.helper.FullscreenKeyboardHelper
import cafe.adriel.verne.presentation.helper.PreferencesHelper
import cafe.adriel.verne.presentation.helper.StatefulLayoutHelper
import cafe.adriel.verne.presentation.ui.editor.EditorViewModel
import cafe.adriel.verne.presentation.ui.main.MainViewModel
import cafe.adriel.verne.presentation.ui.main.explorer.ExplorerViewModel
import cafe.adriel.verne.shared.di.Component
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

class PresentationComponent(private val appContext: Context) : Component {

    private val viewModelModule = module {
        viewModel { MainViewModel(get()) }
        viewModel { ExplorerViewModel(appContext, get(), get(), get(), get(), get(), get(), get()) }
        viewModel { EditorViewModel(appContext, get(), get(), get(), get()) }
    }

    private val helperModule = module {
        single { PreferencesHelper(get()) }
        single { AnalyticsHelper(appContext, get()) }
        single { CustomTabsHelper(appContext) }
        factory { FullscreenKeyboardHelper() }
        factory { StatefulLayoutHelper() }
    }

    override fun getModules() = viewModelModule + helperModule
}
