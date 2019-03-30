package cafe.adriel.verne.interactor.di

import cafe.adriel.verne.interactor.explorer.CreateItemExplorerInteractor
import cafe.adriel.verne.interactor.explorer.ItemTextExplorerInteractor
import cafe.adriel.verne.interactor.explorer.MoveItemExplorerInteractor
import cafe.adriel.verne.interactor.explorer.RenameItemExplorerInteractor
import cafe.adriel.verne.interactor.explorer.SearchItemsExplorerInteractor
import cafe.adriel.verne.interactor.explorer.SelectItemsExplorerInteractor
import cafe.adriel.verne.shared.di.Component
import org.koin.dsl.module

class InteractorComponent : Component {

    private val explorerModule = module {
        single { SearchItemsExplorerInteractor(get()) }
        single { SelectItemsExplorerInteractor(get(), get()) }
        single { CreateItemExplorerInteractor(get()) }
        single { MoveItemExplorerInteractor(get()) }
        single { RenameItemExplorerInteractor(get()) }
        single { ItemTextExplorerInteractor(get()) }
    }

    override fun getModules() = listOf(explorerModule)
}
