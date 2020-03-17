package cafe.adriel.verne.data.internal.mapper.data

import cafe.adriel.verne.data.internal.ktx.toGzip
import cafe.adriel.verne.data.internal.mapper.Mapper
import cafe.adriel.verne.data.internal.model.FileContentDto
import cafe.adriel.verne.domain.model.NewFileContent
import com.google.firebase.Timestamp
import com.google.firebase.firestore.Blob

internal object NewFileContentToDataMapper : Mapper<NewFileContent, FileContentDto> {

    override fun invoke(input: NewFileContent): FileContentDto = input.run {
        FileContentDto(
            content = Blob.fromBytes(
                if (content.isBlank()) ByteArray(size = 0)
                else content.toGzip()
            ),
            createdAt = Timestamp.now()
        )
    }
}
