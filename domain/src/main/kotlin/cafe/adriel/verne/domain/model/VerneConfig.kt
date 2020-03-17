package cafe.adriel.verne.domain.model

data class VerneConfig(
    val releaseBuild: Boolean,
    val appName: String,
    val versionName: String,
    val versionCode: Int
)
