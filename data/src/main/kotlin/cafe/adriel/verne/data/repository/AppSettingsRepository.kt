package cafe.adriel.verne.data.repository

import cafe.adriel.pufferdb.core.PufferDB
import cafe.adriel.verne.domain.repository.SettingsRepository
import cafe.adriel.verne.domain.repository.SettingsRepository.Companion.DEFAULT_FONT_FAMILY
import cafe.adriel.verne.domain.repository.SettingsRepository.Companion.DEFAULT_FONT_SIZE
import cafe.adriel.verne.domain.repository.SettingsRepository.Companion.DEFAULT_MARGIN_SIZE
import cafe.adriel.verne.shared.extension.withIo
import cafe.adriel.verne.shared.model.AppConfig

class AppSettingsRepository(private val appConfig: AppConfig) : SettingsRepository {

    companion object {
        private const val KEY_FONT_FAMILY = "editorFontFamily"
        private const val KEY_FONT_SIZE = "editorFontSize"
        private const val KEY_MARGIN_SIZE = "editorMarginSize"
    }

    private val settings by lazy { PufferDB.with(appConfig.settingsFile) }

    override suspend fun getFontFamily(): String = withIo {
        settings.get(KEY_FONT_FAMILY, DEFAULT_FONT_FAMILY)
    }

    override suspend fun setFontFamily(newValue: String) {
        settings.put(KEY_FONT_FAMILY, newValue)
    }

    override suspend fun getFontSize() = withIo {
        settings.get(KEY_FONT_SIZE, DEFAULT_FONT_SIZE)
    }

    override suspend fun setFontSize(newValue: Int) {
        settings.put(KEY_FONT_SIZE, newValue)
    }

    override suspend fun getMarginSize() = withIo {
        settings.get(KEY_MARGIN_SIZE, DEFAULT_MARGIN_SIZE)
    }

    override suspend fun setMarginSize(newValue: Int) {
        settings.put(KEY_MARGIN_SIZE, newValue)
    }
}
