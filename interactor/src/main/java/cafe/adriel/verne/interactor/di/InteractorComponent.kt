package cafe.adriel.verne.interactor.di

import cafe.adriel.verne.interactor.preference.FontFamilyPreferenceInteractor
import cafe.adriel.verne.interactor.preference.FontSizePreferenceInteractor
import cafe.adriel.verne.interactor.preference.MarginSizePreferenceInteractor
import cafe.adriel.verne.shared.di.Component
import org.koin.dsl.module

class InteractorComponent : Component {

    private val repositoryModule = module {
        // TODO
    }

    private val preferenceModule = module {
        single { FontFamilyPreferenceInteractor(get()) }
        single { FontSizePreferenceInteractor(get()) }
        single { MarginSizePreferenceInteractor(get()) }
    }

    override fun getModules() = listOf(repositoryModule, preferenceModule)
}