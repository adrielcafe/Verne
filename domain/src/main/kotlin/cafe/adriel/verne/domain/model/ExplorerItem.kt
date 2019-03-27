package cafe.adriel.verne.domain.model

sealed class ExplorerItem(open val path: String) {

    data class Folder(override val path: String) : ExplorerItem(path)

    data class File(override val path: String) : ExplorerItem(path)

    val file by lazy { java.io.File(path) }
    val title by lazy { file.nameWithoutExtension }
    val isDeleted by lazy { file.isHidden }
    val isEmpty by lazy { file.length() == 0L }

    // TODO move to other place
//    val pathAfterBaseDir by lazy {
//        val path = file.parent.substringAfter(App.BASE_DIR_NAME, "/")
//        if (path.isBlank()) "/" else path
//    }
}
