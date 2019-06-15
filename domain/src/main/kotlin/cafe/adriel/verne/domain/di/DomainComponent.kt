package cafe.adriel.verne.domain.di

import cafe.adriel.verne.domain.interactor.explorer.CreateItemExplorerInteractor
import cafe.adriel.verne.domain.interactor.explorer.ItemTextExplorerInteractor
import cafe.adriel.verne.domain.interactor.explorer.MoveItemExplorerInteractor
import cafe.adriel.verne.domain.interactor.explorer.RenameItemExplorerInteractor
import cafe.adriel.verne.domain.interactor.explorer.SearchItemsExplorerInteractor
import cafe.adriel.verne.domain.interactor.explorer.SelectItemsExplorerInteractor
import cafe.adriel.verne.domain.interactor.settings.FontFamilySettingsInteractor
import cafe.adriel.verne.domain.interactor.settings.FontSizeSettingsInteractor
import cafe.adriel.verne.domain.interactor.settings.MarginSizeSettingsInteractor
import cafe.adriel.verne.shared.di.Component
import org.koin.dsl.module

class DomainComponent : Component {

    private val explorerInteractorModule = module {
        single { SearchItemsExplorerInteractor(get()) }
        single { SelectItemsExplorerInteractor(get(), get()) }
        single { CreateItemExplorerInteractor(get()) }
        single { MoveItemExplorerInteractor(get()) }
        single { RenameItemExplorerInteractor(get()) }
        single { ItemTextExplorerInteractor(get()) }
    }

    private val settingsInteractorModule = module {
        single { FontFamilySettingsInteractor(get()) }
        single { FontSizeSettingsInteractor(get()) }
        single { MarginSizeSettingsInteractor(get()) }
    }

    override fun getModules() = listOf(explorerInteractorModule, settingsInteractorModule)
}
