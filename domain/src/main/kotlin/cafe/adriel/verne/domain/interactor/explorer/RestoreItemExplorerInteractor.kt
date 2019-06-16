package cafe.adriel.verne.domain.interactor.explorer

import cafe.adriel.verne.domain.extension.asExplorerItem
import cafe.adriel.verne.domain.model.ExplorerItem
import cafe.adriel.verne.shared.extension.withDefault
import java.io.File

class RestoreItemExplorerInteractor(private val renameItemInteractor: RenameItemExplorerInteractor) {

    suspend operator fun invoke(item: ExplorerItem) {
        withDefault {
            val deletedFile = File(item.file.parent, ".${item.file.name}")
            when {
                item.isDeleted ->
                    renameItemInteractor(item, item.file.name.removePrefix("."))
                deletedFile.exists() ->
                    renameItemInteractor(deletedFile.asExplorerItem(), deletedFile.name.removePrefix("."))
                else -> Unit
            }
        }
    }
}
