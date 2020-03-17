package cafe.adriel.verne.domain.model

sealed class NewExplorerItem(
    open val name: String,
    open val parent: ExplorerItem.Folder?
) {

    data class Folder(
        override val name: String,
        override val parent: ExplorerItem.Folder?
    ) : NewExplorerItem(
        name = name,
        parent = parent
    )

    data class File(
        override val name: String,
        override val parent: ExplorerItem.Folder?
    ) : NewExplorerItem(
        name = name,
        parent = parent
    )
}
