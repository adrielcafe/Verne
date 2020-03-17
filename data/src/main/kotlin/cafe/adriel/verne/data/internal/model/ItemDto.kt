package cafe.adriel.verne.data.internal.model

import cafe.adriel.verne.data.internal.ktx.generateFirebaseId
import com.google.firebase.Timestamp

internal data class ItemDto(
    val id: String = generateFirebaseId(),
    val parentId: String? = null,
    val contentId: String? = null,
    val name: String = "",
    val type: String = TYPE_FILE,
    val status: String = STATUS_EDITABLE,
    val createdAt: Timestamp = Timestamp.now(),
    val updatedAt: Timestamp? = null
) {

    companion object {

        const val TYPE_FOLDER = "folder"
        const val TYPE_FILE = "file"

        const val STATUS_EDITABLE = "editable"
        const val STATUS_DELETED = "deleted"
    }
}
