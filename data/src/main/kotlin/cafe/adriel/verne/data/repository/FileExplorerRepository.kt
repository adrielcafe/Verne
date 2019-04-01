package cafe.adriel.verne.data.repository

import cafe.adriel.verne.domain.repository.ExplorerRepository
import cafe.adriel.verne.shared.extension.withIo
import cafe.adriel.verne.shared.model.AppConfig
import java.io.File

class FileExplorerRepository(private val appConfig: AppConfig) : ExplorerRepository {

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

    override suspend fun search(query: String, showDeleted: Boolean) = withIo {
        if (query.isBlank()) {
            emptySequence()
        } else {
            appConfig.explorerRootFolder
                .walk()
                .filter { searchFilter(it, query, showDeleted) }
        }
    }

    override suspend fun select(dir: File, showDeleted: Boolean) = withIo {
        (dir.listFiles() ?: emptyArray())
            .asSequence()
            .filter { selectFilter(it, showDeleted) }
    }

    override suspend fun create(file: File, isFolder: Boolean) = withIo {
        if (isFolder) {
            file.mkdirs()
        } else {
            file.createNewFile()
        }
    }

    override suspend fun move(file: File, parentDir: File) = withIo {
        File(parentDir, file.name).also {
            file.renameTo(it)
        }
    }

    override suspend fun rename(file: File, newName: String) = withIo {
        File(file.parent, newName).also {
            file.renameTo(it)
        }
    }

    override suspend fun getText(file: File) = withIo {
        if (file.isFile && file.exists()) {
            file.readText()
        } else {
            ""
        }
    }

    override suspend fun setText(file: File, text: String) = withIo {
        if (file.isFile && file.exists() && file.canWrite()) {
            file.writeText(text)
            true
        } else {
            false
        }
    }
}
