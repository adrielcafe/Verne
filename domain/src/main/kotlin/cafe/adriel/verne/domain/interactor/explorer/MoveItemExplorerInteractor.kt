package cafe.adriel.verne.domain.interactor.explorer

import cafe.adriel.verne.domain.extension.asExplorerItem
import cafe.adriel.verne.domain.model.ExplorerItem
import cafe.adriel.verne.domain.repository.ExplorerRepository
import cafe.adriel.verne.shared.extension.withDefault

class MoveItemExplorerInteractor(private val explorerRepository: ExplorerRepository) {

    suspend operator fun invoke(from: ExplorerItem, to: ExplorerItem.Folder): ExplorerItem = withDefault {
        explorerRepository.move(from.file, to.file).asExplorerItem()
    }
}
