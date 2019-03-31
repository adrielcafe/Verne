package cafe.adriel.verne.domain.interactor.explorer

import cafe.adriel.verne.domain.extension.asExplorerItem
import cafe.adriel.verne.domain.model.ExplorerItem
import cafe.adriel.verne.domain.repository.ExplorerRepository

class RenameItemExplorerInteractor(private val explorerRepository: ExplorerRepository) {

    suspend operator fun invoke(item: ExplorerItem, newName: String): ExplorerItem {
        val newFileName = if (item is ExplorerItem.File && !newName.endsWith(".html", true)) {
            "$newName.html"
        } else {
            newName
        }
        return explorerRepository.rename(item.file, newFileName).asExplorerItem()
    }
}
