package cafe.adriel.verne.domain.interactor.explorer

import cafe.adriel.verne.domain.extension.asExplorerItem
import cafe.adriel.verne.domain.model.BaseDir
import cafe.adriel.verne.domain.model.ExplorerItem
import cafe.adriel.verne.domain.repository.ExplorerRepository

class SelectItemsExplorerInteractor(
    private val baseDir: BaseDir,
    private val explorerRepository: ExplorerRepository
) {

    suspend operator fun invoke(item: ExplorerItem.Folder? = null): List<ExplorerItem> {
        val dir = item?.file ?: baseDir.item.file
        return explorerRepository.select(dir)
            .map { it.asExplorerItem() }
            .toList()
    }
}
