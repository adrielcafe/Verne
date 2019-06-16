package cafe.adriel.verne.shared.error

sealed class FileExplorerError : Throwable() {

    object FileNotCreated : FileExplorerError()
    object FileNotMoved : FileExplorerError()
    object FileNotRenamed : FileExplorerError()
    object FileNotReadable : FileExplorerError()
    object FileNotWritable : FileExplorerError()
}
