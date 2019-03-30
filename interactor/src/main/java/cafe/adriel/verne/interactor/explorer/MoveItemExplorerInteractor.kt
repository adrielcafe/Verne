package cafe.adriel.verne.interactor.explorer

import cafe.adriel.verne.domain.extension.asExplorerItem
import cafe.adriel.verne.domain.model.ExplorerItem
import cafe.adriel.verne.domain.repository.ExplorerRepository

class MoveItemExplorerInteractor(private val explorerRepository: ExplorerRepository) {

    suspend operator fun invoke(from: ExplorerItem, to: ExplorerItem.Folder) =
        explorerRepository.move(from.file, to.file).asExplorerItem()
}
