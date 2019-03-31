package cafe.adriel.verne.domain.interactor.explorer

import cafe.adriel.verne.domain.model.ExplorerItem
import cafe.adriel.verne.domain.repository.ExplorerRepository
import java.io.File

class CreateItemExplorerInteractor(private val explorerRepository: ExplorerRepository) {

    suspend operator fun invoke(parentItem: ExplorerItem.Folder, name: String, isFolder: Boolean): ExplorerItem {
        val fileName = if (!isFolder && !name.endsWith(".html", true)) {
            "$name.html"
        } else {
            name
        }
        val file = File(parentItem.file, fileName)
        val item = if (isFolder) ExplorerItem.Folder(file.path) else ExplorerItem.File(file.path)
        explorerRepository.create(item.file, isFolder)
        return item
    }
}
