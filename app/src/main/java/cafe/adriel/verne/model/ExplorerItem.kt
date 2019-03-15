package cafe.adriel.verne.model

import android.os.Parcelable
import cafe.adriel.verne.App
import kotlinx.android.parcel.Parcelize

sealed class ExplorerItem(open val path: String) : Parcelable {

    @Parcelize
    data class Folder(override val path: String) : ExplorerItem(path)

    @Parcelize
    data class File(override val path: String) : ExplorerItem(path)

    val file by lazy { java.io.File(path) }
    val title by lazy { file.nameWithoutExtension }
    val isDeleted by lazy { file.isHidden }
    val pathAfterBaseDir by lazy {
        val path = file.parent.substringAfter(App.BASE_DIR_NAME, "/")
        if (path.isBlank()) "/" else path
    }
}
