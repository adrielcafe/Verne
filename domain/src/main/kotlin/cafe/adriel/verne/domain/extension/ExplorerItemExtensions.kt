package cafe.adriel.verne.domain.extension

import cafe.adriel.verne.domain.model.BaseDir
import cafe.adriel.verne.domain.model.ExplorerItem
import java.io.File

fun File.asExplorerItem(): ExplorerItem = if (isDirectory) ExplorerItem.Folder(path) else ExplorerItem.File(path)

fun ExplorerItem.getPathAfterBaseDir(baseDir: BaseDir): String {
    val path = file.parent.substringAfter(baseDir.item.file.name, "/")
    return if (path.isBlank()) "/" else path
}