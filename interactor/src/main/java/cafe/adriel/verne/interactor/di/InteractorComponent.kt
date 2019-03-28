package cafe.adriel.verne.interactor.di

import cafe.adriel.verne.interactor.explorer.CreateItemExplorerInteractor
import cafe.adriel.verne.interactor.explorer.ItemTextExplorerInteractor
import cafe.adriel.verne.interactor.explorer.MoveItemExplorerInteractor
import cafe.adriel.verne.interactor.explorer.RenameItemExplorerInteractor
import cafe.adriel.verne.interactor.explorer.SearchItemsExplorerInteractor
import cafe.adriel.verne.interactor.explorer.SelectItemsExplorerInteractor
import cafe.adriel.verne.interactor.preference.FontFamilyPreferenceInteractor
import cafe.adriel.verne.interactor.preference.FontSizePreferenceInteractor
import cafe.adriel.verne.interactor.preference.MarginSizePreferenceInteractor
import cafe.adriel.verne.shared.di.Component
import org.koin.dsl.module

class InteractorComponent : Component {

    private val repositoryModule = module {
        single { SearchItemsExplorerInteractor(get()) }
        single { SelectItemsExplorerInteractor(get(), get()) }
        single { CreateItemExplorerInteractor(get()) }
        single { MoveItemExplorerInteractor(get()) }
        single { RenameItemExplorerInteractor(get()) }
        single { ItemTextExplorerInteractor(get()) }
    }

    private val preferenceModule = module {
        single { FontFamilyPreferenceInteractor(get()) }
        single { FontSizePreferenceInteractor(get()) }
        single { MarginSizePreferenceInteractor(get()) }
    }

    override fun getModules() = listOf(repositoryModule, preferenceModule)
}