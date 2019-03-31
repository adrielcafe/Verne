package cafe.adriel.verne.domain.interactor.explorer

import cafe.adriel.verne.domain.extension.asExplorerItem
import cafe.adriel.verne.domain.repository.ExplorerRepository

class SearchItemsExplorerInteractor(private val explorerRepository: ExplorerRepository) {

    suspend operator fun invoke(query: String) =
        explorerRepository.search(query)
            .map { it.asExplorerItem() }
            .toList()
}
