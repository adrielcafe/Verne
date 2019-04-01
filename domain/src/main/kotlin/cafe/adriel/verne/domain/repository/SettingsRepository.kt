package cafe.adriel.verne.domain.repository

interface SettingsRepository {

    companion object {
        const val DEFAULT_FONT_FAMILY = "ALICE"
        const val DEFAULT_FONT_SIZE = 18
        const val DEFAULT_MARGIN_SIZE = 20
    }

    suspend fun getFontFamily(): String

    suspend fun setFontFamily(newValue: String)

    suspend fun getFontSize(): Int

    suspend fun setFontSize(newValue: Int)

    suspend fun getMarginSize(): Int

    suspend fun setMarginSize(newValue: Int)
}
