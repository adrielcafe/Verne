package cafe.adriel.verne.domain.model

import java.util.Date

sealed class ExplorerItem(
    open val id: String,
    open val parentId: String?,
    open val name: String,
    open val createdAt: Date,
    open val updatedAt: Date?
) {

    data class Folder(
        override val id: String,
        override val parentId: String?,
        override val name: String,
        override val createdAt: Date,
        override val updatedAt: Date?,
        val filesCount: Int
    ) : ExplorerItem(
        id = id,
        parentId = parentId,
        name = name,
        createdAt = createdAt,
        updatedAt = updatedAt
    )

    data class File(
        override val id: String,
        override val parentId: String?,
        override val name: String,
        override val createdAt: Date,
        override val updatedAt: Date?,
        val contentId: String
    ) : ExplorerItem(
        id = id,
        parentId = parentId,
        name = name,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}
