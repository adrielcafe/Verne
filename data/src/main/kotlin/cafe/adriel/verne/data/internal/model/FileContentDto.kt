package cafe.adriel.verne.data.internal.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.Blob

internal data class FileContentDto(
    val content: Blob = Blob.fromBytes(ByteArray(size = 0)),
    val createdAt: Timestamp = Timestamp.now()
)
