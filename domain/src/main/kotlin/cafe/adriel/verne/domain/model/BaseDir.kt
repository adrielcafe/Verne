package cafe.adriel.verne.domain.model

import java.io.File

inline class BaseDir(val path: String) {
     val file: File
          get() = File(path)
     val item: ExplorerItem.Folder
          get() = ExplorerItem.Folder(path)
}