package cafe.adriel.verne.data.di

import cafe.adriel.verne.data.repository.FileExplorerRepository
import cafe.adriel.verne.domain.repository.ExplorerRepository
import cafe.adriel.verne.shared.di.Component
import org.koin.dsl.module

class DataComponent : Component {

    private val repositoryModule = module {
        single<ExplorerRepository> { FileExplorerRepository(get()) }
    }

    override fun getModules() = listOf(repositoryModule)
}
