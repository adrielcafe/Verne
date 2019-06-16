package cafe.adriel.verne.shared.model

import java.io.File

data class AppConfig(
    val versionCode: Int,
    val versionName: String,
    val isDebug: Boolean,
    val explorerRootFolder: File
)
