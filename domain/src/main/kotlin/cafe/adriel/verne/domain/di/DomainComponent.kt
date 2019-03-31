package cafe.adriel.verne.domain.di

import cafe.adriel.verne.domain.interactor.explorer.CreateItemExplorerInteractor
import cafe.adriel.verne.domain.interactor.explorer.ItemTextExplorerInteractor
import cafe.adriel.verne.domain.interactor.explorer.MoveItemExplorerInteractor
import cafe.adriel.verne.domain.interactor.explorer.RenameItemExplorerInteractor
import cafe.adriel.verne.domain.interactor.explorer.SearchItemsExplorerInteractor
import cafe.adriel.verne.domain.interactor.explorer.SelectItemsExplorerInteractor
import cafe.adriel.verne.shared.di.Component
import org.koin.dsl.module

class DomainComponent : Component {

    private val interactorModule = module {
        single { SearchItemsExplorerInteractor(get()) }
        single { SelectItemsExplorerInteractor(get(), get()) }
        single { CreateItemExplorerInteractor(get()) }
        single { MoveItemExplorerInteractor(get()) }
        single { RenameItemExplorerInteractor(get()) }
        single { ItemTextExplorerInteractor(get()) }
    }

    override fun getModules() = listOf(interactorModule)
}
