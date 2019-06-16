package cafe.adriel.verne.domain.interactor.explorer

import cafe.adriel.verne.domain.model.ExplorerItem
import cafe.adriel.verne.domain.repository.ExplorerRepository
import cafe.adriel.verne.shared.extension.withDefault
import java.io.File

class CreateItemExplorerInteractor(private val explorerRepository: ExplorerRepository) {

    suspend operator fun invoke(parentItem: ExplorerItem.Folder, name: String, isFolder: Boolean): ExplorerItem =
        withDefault {
            val fileName = if (!isFolder && !name.endsWith(".html", true)) {
                "$name.html"
            } else {
                name
            }
            val file = File(parentItem.file, fileName)
            explorerRepository.create(file, isFolder)

            if (isFolder) ExplorerItem.Folder(file.path) else ExplorerItem.File(file.path)
        }
}
