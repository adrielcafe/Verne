package cafe.adriel.verne.domain.di

import cafe.adriel.verne.domain.interactor.explorer.CreateItemExplorerInteractor
import cafe.adriel.verne.domain.interactor.explorer.ItemTextExplorerInteractor
import cafe.adriel.verne.domain.interactor.explorer.MoveItemExplorerInteractor
import cafe.adriel.verne.domain.interactor.explorer.RenameItemExplorerInteractor
import cafe.adriel.verne.domain.interactor.explorer.RestoreItemExplorerInteractor
import cafe.adriel.verne.domain.interactor.explorer.SearchItemsExplorerInteractor
import cafe.adriel.verne.domain.interactor.explorer.SelectItemsExplorerInteractor
import cafe.adriel.verne.domain.interactor.explorer.SoftDeleteItemExplorerInteractor
import cafe.adriel.verne.domain.interactor.settings.FontFamilySettingsInteractor
import cafe.adriel.verne.domain.interactor.settings.FontSizeSettingsInteractor
import cafe.adriel.verne.domain.interactor.settings.MarginSizeSettingsInteractor
import cafe.adriel.verne.shared.di.Component
import org.koin.dsl.module

class DomainComponent : Component {

    private val explorerInteractorModule = module {
        single { ItemTextExplorerInteractor(get()) }
        single { SearchItemsExplorerInteractor(get()) }
        single { SelectItemsExplorerInteractor(get(), get()) }
        single { CreateItemExplorerInteractor(get()) }
        single { MoveItemExplorerInteractor(get()) }
        single { RenameItemExplorerInteractor(get()) }
        single { SoftDeleteItemExplorerInteractor(get()) }
        single { RestoreItemExplorerInteractor(get()) }
    }

    private val settingsInteractorModule = module {
        single { FontFamilySettingsInteractor(get()) }
        single { FontSizeSettingsInteractor(get()) }
        single { MarginSizeSettingsInteractor(get()) }
    }

    override fun getModules() = explorerInteractorModule + settingsInteractorModule
}
