package cafe.adriel.verne.data.di

import cafe.adriel.verne.data.repository.AppSettingsRepository
import cafe.adriel.verne.data.repository.LocalExplorerRepository
import cafe.adriel.verne.domain.repository.ExplorerRepository
import cafe.adriel.verne.domain.repository.SettingsRepository
import cafe.adriel.verne.shared.di.Component
import org.koin.dsl.module

class DataComponent : Component {

    private val repositoryModule = module {
        single<ExplorerRepository> { LocalExplorerRepository(get()) }
        single<SettingsRepository> { AppSettingsRepository(get()) }
    }

    override fun getModules() = listOf(repositoryModule)
}