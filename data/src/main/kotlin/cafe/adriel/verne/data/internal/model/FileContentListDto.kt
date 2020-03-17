package cafe.adriel.verne.data.internal.model

internal data class FileContentListDto(
    val versions: List<FileContentDto> = emptyList()
)
