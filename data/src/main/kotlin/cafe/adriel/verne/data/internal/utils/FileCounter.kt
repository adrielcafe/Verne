package cafe.adriel.verne.data.internal.utils

import cafe.adriel.verne.data.internal.model.ItemDto

internal class FileCounter {

    operator fun invoke(item: ItemDto, items: Sequence<ItemDto>): Int =
        item.filesCount(items)

    private fun ItemDto.filesCount(items: Sequence<ItemDto>, counter: Int = 0): Int =
        when (type) {
            ItemDto.TYPE_FOLDER -> {
                val (folders, files) = items
                    .filter { it.parentId == id && it.status == ItemDto.STATUS_EDITABLE }
                    .partition { it.type == ItemDto.TYPE_FOLDER }

                counter + files.size + folders.fold(0) { acc, folder -> folder.filesCount(items, acc) }
            }
            else -> counter
        }
}
