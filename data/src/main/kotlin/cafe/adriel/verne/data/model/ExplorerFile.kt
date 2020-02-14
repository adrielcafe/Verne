package cafe.adriel.verne.data.model

import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.annotation.Index
import io.objectbox.annotation.IndexType
import io.objectbox.relation.ToOne

@Entity
internal data class ExplorerFile(
    @Id
    var id: Long = 0,
    @Index(type = IndexType.VALUE)
    var name: String = "",
    var content: String = "",
    @Index
    var deleted: Boolean = false,
    var createdAt: Long = System.currentTimeMillis(),
    var updatedAt: Long? = null
) {

    lateinit var parent: ToOne<ExplorerFolder>
}
