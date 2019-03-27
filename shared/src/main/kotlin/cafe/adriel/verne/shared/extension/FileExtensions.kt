package cafe.adriel.verne.shared.extension

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileFilter

private val fileFilter by lazy {
    FileFilter { !it.isDirectory && !it.isHidden }
}

suspend fun File.filesCount() = withContext(Dispatchers.IO) {
    walk().fold(0) { acc, file ->
        acc + (file.listFiles(fileFilter)?.size ?: 0)
    }
}