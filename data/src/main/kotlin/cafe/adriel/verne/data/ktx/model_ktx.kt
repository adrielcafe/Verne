package cafe.adriel.verne.data.ktx

import cafe.adriel.verne.data.model.ExplorerFile
import cafe.adriel.verne.data.model.ExplorerFolder
import io.objectbox.relation.ToMany
import io.objectbox.relation.ToOne

internal suspend fun ExplorerFolder.getFilesCount(): Int = getFilesCount(childFolders) + childFiles.size

internal suspend fun ExplorerFolder.getPath(): String = getPath(parent)

internal suspend fun ExplorerFile.getPath(): String = getPath(parent)

private suspend fun getFilesCount(relation: ToMany<ExplorerFolder>, filesCount: Int = 0): Int =
    if (relation.isEmpty()) filesCount
    else relation.fold(filesCount) { counter, childFolder ->
        counter + childFolder.childFiles.size + childFolder.getFilesCount()
    }

private tailrec suspend fun getPath(relation: ToOne<ExplorerFolder>, path: String = ""): String =
    if (relation.isNull) path
    else getPath(relation = relation.target.parent, path = "/${relation.target.name}$path")
