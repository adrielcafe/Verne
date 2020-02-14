package cafe.adriel.verne.domain.model

import java.io.File

data class VerneConfig(
    val releaseBuild: Boolean,
    val versionCode: Int,
    val versionName: String,
    val databaseDir: File
) {

    companion object {

        const val DATABASE_NAME = "verne.box"
    }
}
