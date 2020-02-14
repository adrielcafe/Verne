package cafe.adriel.verne.data.model

import io.objectbox.annotation.Backlink
import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.annotation.Index
import io.objectbox.annotation.IndexType
import io.objectbox.relation.ToMany
import io.objectbox.relation.ToOne

@Entity
internal data class ExplorerFolder(
    @Id
    var id: Long = 0,
    @Index(type = IndexType.VALUE)
    var name: String = "",
    @Index
    var deleted: Boolean = false,
    var createdAt: Long = System.currentTimeMillis(),
    var updatedAt: Long? = null
) {

    lateinit var parent: ToOne<ExplorerFolder>

    @Backlink
    lateinit var childFolders: ToMany<ExplorerFolder>

    @Backlink
    lateinit var childFiles: ToMany<ExplorerFile>
}
