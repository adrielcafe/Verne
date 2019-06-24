package cafe.adriel.verne.domain.interactor.explorer

import cafe.adriel.verne.domain.extension.asExplorerItem
import cafe.adriel.verne.domain.repository.ExplorerRepository
import cafe.adriel.verne.shared.extension.withIO
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList

class SearchItemsExplorerInteractor(private val explorerRepository: ExplorerRepository) {

    suspend operator fun invoke(query: String) = withIO {
        explorerRepository.search(query)
            .map { it.asExplorerItem() }
            .toList()
    }
}
