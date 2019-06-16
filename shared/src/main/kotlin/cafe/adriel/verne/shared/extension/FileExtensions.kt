package cafe.adriel.verne.shared.extension

import java.io.File
import java.io.FileFilter

private val fileFilter by lazy {
    FileFilter { !it.isDirectory && !it.isHidden }
}

suspend fun File.filesCount() = withIO {
    walk().fold(0) { total, file ->
        val count = file.listFiles(fileFilter)?.size ?: 0
        total + count
    }
}
