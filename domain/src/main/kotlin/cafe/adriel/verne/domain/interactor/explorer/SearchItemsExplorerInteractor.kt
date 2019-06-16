package cafe.adriel.verne.domain.interactor.explorer

import cafe.adriel.verne.domain.extension.asExplorerItem
import cafe.adriel.verne.domain.repository.ExplorerRepository
import cafe.adriel.verne.shared.extension.withDefault

class SearchItemsExplorerInteractor(private val explorerRepository: ExplorerRepository) {

    suspend operator fun invoke(query: String) = withDefault {
        explorerRepository.search(query)
            .map { it.asExplorerItem() }
            .toList()
    }
}
