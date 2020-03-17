package cafe.adriel.verne.data.internal.mapper.domain

import cafe.adriel.verne.data.internal.ktx.fromGzip
import cafe.adriel.verne.data.internal.mapper.Mapper
import cafe.adriel.verne.data.internal.model.FileContentDto
import cafe.adriel.verne.domain.model.FileContent

internal object FileContentToDomainMapper :
    Mapper<FileContentDto, FileContent> {

    override fun invoke(input: FileContentDto): FileContent = input.run {
        FileContent(
            content = content.toBytes().run {
                if (isEmpty()) ""
                else fromGzip()
            },
            createdAt = createdAt.toDate()
        )
    }
}
