package cafe.adriel.verne.data.repository

import cafe.adriel.verne.domain.repository.ExplorerRepository
import cafe.adriel.verne.shared.error.FileExplorerError
import cafe.adriel.verne.shared.extension.tryOrThrow
import cafe.adriel.verne.shared.extension.withIO
import cafe.adriel.verne.shared.model.AppConfig
import java.io.File

internal class FileExplorerRepository(private val appConfig: AppConfig) : ExplorerRepository {

    init {
        appConfig.explorerRootFolder.mkdirs()
    }

    private val searchFilter = { file: File, query: String, showDeleted: Boolean ->
        file.isFile &&
        file.nameWithoutExtension.contains(query, true) &&
        (!file.isHidden || showDeleted)
    }
    private val selectFilter = { file: File, showDeleted: Boolean ->
        !file.isHidden || showDeleted
    }

    override suspend fun search(query: String, showDeleted: Boolean) = withIO {
        if (query.isBlank()) {
            emptySequence()
        } else {
            appConfig.explorerRootFolder
                .walk()
                .filter { searchFilter(it, query, showDeleted) }
        }
    }

    override suspend fun select(dir: File, showDeleted: Boolean) = withIO {
        (dir.listFiles() ?: emptyArray())
            .asSequence()
            .filter { selectFilter(it, showDeleted) }
    }

    override suspend fun create(file: File, isFolder: Boolean) = withIO {
        tryOrThrow(FileExplorerError.FileNotCreated) {
            if (isFolder) file.mkdirs() else file.createNewFile()
        }
    }

    override suspend fun move(file: File, parentDir: File) = withIO {
        File(parentDir, file.name).also { newFile ->
            tryOrThrow(FileExplorerError.FileNotMoved) {
                file.renameTo(newFile)
            }
        }
    }

    override suspend fun rename(file: File, newName: String) = withIO {
        File(file.parent, newName).also { newFile ->
            tryOrThrow(FileExplorerError.FileNotRenamed) {
                file.renameTo(newFile)
            }
        }
    }

    override suspend fun getText(file: File) = withIO {
        if (file.isFile && file.exists()) {
            tryOrThrow(FileExplorerError.FileNotReadable) {
                file.readText()
            }
        } else {
            ""
        }
    }

    override suspend fun setText(file: File, text: String) = withIO {
        if (file.isFile && file.exists() && file.canWrite()) {
            tryOrThrow(FileExplorerError.FileNotWritable) {
                file.writeText(text)
            }
            true
        } else {
            false
        }
    }
}
