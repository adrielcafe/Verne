package cafe.adriel.verne.data.internal.mapper.data

import cafe.adriel.verne.data.internal.ktx.generateFirebaseId
import cafe.adriel.verne.data.internal.mapper.Mapper
import cafe.adriel.verne.data.internal.model.ItemDto
import cafe.adriel.verne.domain.model.NewExplorerItem
import com.google.firebase.Timestamp

internal object NewItemToDataMapper : Mapper<NewExplorerItem, ItemDto> {

    override fun invoke(input: NewExplorerItem): ItemDto = input.run {
        when (this) {
            is NewExplorerItem.Folder -> ItemDto(
                parentId = parent?.id,
                name = name,
                type = ItemDto.TYPE_FOLDER,
                createdAt = Timestamp.now()
            )
            is NewExplorerItem.File -> ItemDto(
                parentId = parent?.id,
                contentId = generateFirebaseId(),
                name = name,
                type = ItemDto.TYPE_FILE,
                createdAt = Timestamp.now()
            )
        }
    }
}
