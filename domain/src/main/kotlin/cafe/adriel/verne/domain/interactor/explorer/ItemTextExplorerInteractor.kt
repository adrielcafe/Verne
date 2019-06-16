package cafe.adriel.verne.domain.interactor.explorer

import cafe.adriel.verne.domain.model.ExplorerItem
import cafe.adriel.verne.domain.repository.ExplorerRepository
import cafe.adriel.verne.shared.extension.withDefault

class ItemTextExplorerInteractor(private val explorerRepository: ExplorerRepository) {

    suspend fun get(item: ExplorerItem.File): String = withDefault {
        explorerRepository.getText(item.file)
    }

    suspend fun set(item: ExplorerItem.File, newText: String): Boolean = withDefault {
        if (!item.file.exists()) {
            explorerRepository.create(item.file, false)
        }
        explorerRepository.setText(item.file, newText)
    }
}
