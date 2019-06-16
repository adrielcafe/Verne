package cafe.adriel.verne.domain.interactor.explorer

import cafe.adriel.verne.domain.extension.asExplorerItem
import cafe.adriel.verne.domain.model.ExplorerItem
import cafe.adriel.verne.domain.repository.ExplorerRepository
import cafe.adriel.verne.shared.extension.withDefault
import cafe.adriel.verne.shared.model.AppConfig

class SelectItemsExplorerInteractor(
    private val appConfig: AppConfig,
    private val explorerRepository: ExplorerRepository
) {

    suspend operator fun invoke(item: ExplorerItem.Folder? = null): List<ExplorerItem> = withDefault {
        val dir = item?.file ?: appConfig.explorerRootFolder
        explorerRepository.select(dir)
            .map { it.asExplorerItem() }
            .toList()
    }
}
