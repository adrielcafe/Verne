package cafe.adriel.verne.di

import android.app.Application
import android.preference.PreferenceManager
import cafe.adriel.verne.repository.LocalExplorerRepository
import cafe.adriel.verne.view.editor.EditorViewModel
import cafe.adriel.verne.view.editor.typography.TypographyViewModel
import cafe.adriel.verne.view.main.MainViewModel
import cafe.adriel.verne.view.main.explorer.ExplorerViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

class AppComponent(private val app: Application) : Component {

    private val preferenceModule = module {
        single { PreferenceManager.getDefaultSharedPreferences(app.applicationContext) }
    }
    private val repositoryModule = module {
        single { LocalExplorerRepository(app.applicationContext) }
    }
    private val viewModelModule = module {
        viewModel { MainViewModel() }
        viewModel { ExplorerViewModel(app.applicationContext, get()) }
        viewModel { EditorViewModel(app.applicationContext, get(), get()) }
        viewModel { TypographyViewModel(get()) }
    }
    
    override fun getModules() = listOf(preferenceModule, repositoryModule, viewModelModule)

}