package cafe.adriel.verne.data.internal.mapper.domain

import cafe.adriel.verne.data.internal.ktx.generateFirebaseId
import cafe.adriel.verne.data.internal.mapper.Mapper
import cafe.adriel.verne.data.internal.model.ItemDto
import cafe.adriel.verne.domain.model.ExplorerItem

internal object ItemToDomainMapper :
    Mapper<ItemDto, ExplorerItem> {

    override fun invoke(input: ItemDto): ExplorerItem = input.run {
        when (type) {
            ItemDto.TYPE_FOLDER -> ExplorerItem.Folder(
                id = id,
                parentId = parentId,
                name = name,
                createdAt = createdAt.toDate(),
                updatedAt = updatedAt?.toDate(),
                filesCount = 0
            )
            ItemDto.TYPE_FILE -> ExplorerItem.File(
                id = id,
                parentId = parentId,
                contentId = contentId ?: generateFirebaseId(),
                name = name,
                createdAt = createdAt.toDate(),
                updatedAt = updatedAt?.toDate()
            )
            else -> throw TypeCastException("Invalid item type -> $type")
        }
    }
}
