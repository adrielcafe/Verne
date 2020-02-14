package cafe.adriel.verne.domain.model

sealed class NewExplorerItem(
    open val name: String,
    open val parentFolderId: Long?
) {

    data class Folder(
        override val name: String,
        override val parentFolderId: Long?
    ) : NewExplorerItem(
        name = name,
        parentFolderId = parentFolderId
    )

    data class File(
        override val name: String,
        val content: String,
        override val parentFolderId: Long?
    ) : NewExplorerItem(
        name = name,
        parentFolderId = parentFolderId
    )
}
