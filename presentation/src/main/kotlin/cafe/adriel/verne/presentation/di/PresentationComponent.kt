package cafe.adriel.verne.presentation.di

import android.content.Context
import cafe.adriel.verne.presentation.ui.editor.EditorViewModel
import cafe.adriel.verne.presentation.ui.editor.typography.TypographyViewModel
import cafe.adriel.verne.presentation.ui.main.MainViewModel
import cafe.adriel.verne.presentation.ui.main.explorer.ExplorerViewModel
import cafe.adriel.verne.shared.di.Component
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

class PresentationComponent(private val appContext: Context) : Component {

    private val viewModelModule = module {
        viewModel { MainViewModel() }
        viewModel { ExplorerViewModel(appContext, get()) }
        viewModel { EditorViewModel(appContext, get(), get()) }
        viewModel { TypographyViewModel(get()) }
    }

    override fun getModules() = listOf(viewModelModule)
}