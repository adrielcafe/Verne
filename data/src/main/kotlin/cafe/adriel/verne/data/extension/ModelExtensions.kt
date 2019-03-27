package cafe.adriel.verne.data.extension

import cafe.adriel.verne.domain.model.ExplorerItem
import java.io.File

fun File.asExplorerItem(): ExplorerItem = if (isDirectory) ExplorerItem.Folder(path) else ExplorerItem.File(path)