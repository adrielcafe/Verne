package cafe.adriel.verne.domain.model

inline class BaseDir(val path: String) {
    val item
        get() = ExplorerItem.Folder(path)
}
