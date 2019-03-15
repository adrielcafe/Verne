package cafe.adriel.verne.di

import android.content.Context
import android.preference.PreferenceManager
import cafe.adriel.verne.repository.LocalExplorerRepository
import cafe.adriel.verne.view.editor.EditorViewModel
import cafe.adriel.verne.view.editor.typography.TypographyViewModel
import cafe.adriel.verne.view.main.MainViewModel
import cafe.adriel.verne.view.main.explorer.ExplorerViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

class AppComponent(private val appContext: Context) : Component {

    private val preferenceModule = module {
        single { PreferenceManager.getDefaultSharedPreferences(appContext) }
    }
    private val repositoryModule = module {
        single { LocalExplorerRepository(appContext) }
    }
    private val viewModelModule = module {
        viewModel { MainViewModel() }
        viewModel { ExplorerViewModel(appContext, get()) }
        viewModel { EditorViewModel(appContext, get(), get()) }
        viewModel { TypographyViewModel(get()) }
    }

    override fun getModules() = listOf(preferenceModule, repositoryModule, viewModelModule)
}
