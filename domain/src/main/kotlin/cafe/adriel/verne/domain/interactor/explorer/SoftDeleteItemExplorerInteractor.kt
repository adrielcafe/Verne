package cafe.adriel.verne.domain.interactor.explorer

import cafe.adriel.verne.domain.model.ExplorerItem
import cafe.adriel.verne.shared.extension.withDefault

class SoftDeleteItemExplorerInteractor(private val renameItemInteractor: RenameItemExplorerInteractor) {

    suspend operator fun invoke(item: ExplorerItem) = withDefault {
        if (!item.isDeleted) {
            renameItemInteractor(item, ".${item.file.name}")
        }
    }
}
