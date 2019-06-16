package cafe.adriel.verne.domain.interactor.explorer

import cafe.adriel.verne.domain.extension.asExplorerItem
import cafe.adriel.verne.domain.model.ExplorerItem
import cafe.adriel.verne.domain.repository.ExplorerRepository
import cafe.adriel.verne.shared.extension.withDefault

class RenameItemExplorerInteractor(private val explorerRepository: ExplorerRepository) {

    suspend operator fun invoke(item: ExplorerItem, newName: String): ExplorerItem = withDefault {
        val newFileName = if (item is ExplorerItem.File && !newName.endsWith(".html", true)) {
            "$newName.html"
        } else {
            newName
        }
        explorerRepository.rename(item.file, newFileName).asExplorerItem()
    }
}
