package cafe.adriel.verne.domain.model

import java.util.Date

sealed class ExplorerItem(
    open val id: Long,
    open val name: String,
    open val path: String,
    open val deleted: Boolean,
    open val createdAt: Date,
    open val updatedAt: Date?
) {

    data class Folder(
        override val id: Long,
        override val name: String,
        override val path: String,
        override val deleted: Boolean,
        override val createdAt: Date,
        override val updatedAt: Date? = null,
        val filesCount: Int
    ) : ExplorerItem(
        id = id,
        name = name,
        path = path,
        deleted = deleted,
        createdAt = createdAt,
        updatedAt = updatedAt
    )

    data class File(
        override val id: Long,
        override val name: String,
        override val path: String,
        override val deleted: Boolean,
        override val createdAt: Date,
        override val updatedAt: Date? = null,
        val content: String
    ) : ExplorerItem(
        id = id,
        name = name,
        path = path,
        deleted = deleted,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}
