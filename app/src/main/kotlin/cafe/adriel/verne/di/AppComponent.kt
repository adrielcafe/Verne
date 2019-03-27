package cafe.adriel.verne.di

import android.content.Context
import android.preference.PreferenceManager
import cafe.adriel.verne.App
import cafe.adriel.verne.domain.repository.ExplorerRepository
import cafe.adriel.verne.data.repository.LocalExplorerRepository
import cafe.adriel.verne.domain.model.BaseDir
import cafe.adriel.verne.presentation.ui.editor.EditorViewModel
import cafe.adriel.verne.presentation.ui.editor.typography.TypographyViewModel
import cafe.adriel.verne.presentation.ui.main.MainViewModel
import cafe.adriel.verne.presentation.ui.main.explorer.ExplorerViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import java.io.File

class AppComponent(private val appContext: Context) : Component {

    // TODO move to a better place
    private val baseDir = File(appContext.filesDir, App.BASE_DIR_NAME)

    private val preferenceModule = module {
        single { PreferenceManager.getDefaultSharedPreferences(appContext) }
    }
    private val repositoryModule = module {
        single<ExplorerRepository> { LocalExplorerRepository(BaseDir(baseDir.path)) }
    }
    private val viewModelModule = module {
        viewModel { MainViewModel() }
        viewModel { ExplorerViewModel(appContext, get()) }
        viewModel { EditorViewModel(appContext, get(), get()) }
        viewModel { TypographyViewModel(get()) }
    }

    override fun getModules() = listOf(preferenceModule, repositoryModule, viewModelModule)
}
