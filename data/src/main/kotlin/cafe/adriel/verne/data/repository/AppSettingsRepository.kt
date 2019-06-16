package cafe.adriel.verne.data.repository

import cafe.adriel.pufferdb.core.Puffer
import cafe.adriel.verne.domain.repository.SettingsRepository
import cafe.adriel.verne.domain.repository.SettingsRepository.Companion.DEFAULT_FONT_FAMILY
import cafe.adriel.verne.domain.repository.SettingsRepository.Companion.DEFAULT_FONT_SIZE
import cafe.adriel.verne.domain.repository.SettingsRepository.Companion.DEFAULT_MARGIN_SIZE
import cafe.adriel.verne.shared.extension.withIO

internal class AppSettingsRepository(private val settings: Puffer) : SettingsRepository {

    companion object {
        private const val KEY_FONT_FAMILY = "editorFontFamily"
        private const val KEY_FONT_SIZE = "editorFontSize"
        private const val KEY_MARGIN_SIZE = "editorMarginSize"
    }

    override suspend fun getFontFamily(): String = withIO {
        settings.get(KEY_FONT_FAMILY, DEFAULT_FONT_FAMILY)
    }

    override suspend fun setFontFamily(newValue: String) = withIO {
        settings.put(KEY_FONT_FAMILY, newValue)
    }

    override suspend fun getFontSize() = withIO {
        settings.get(KEY_FONT_SIZE, DEFAULT_FONT_SIZE)
    }

    override suspend fun setFontSize(newValue: Int) = withIO {
        settings.put(KEY_FONT_SIZE, newValue)
    }

    override suspend fun getMarginSize() = withIO {
        settings.get(KEY_MARGIN_SIZE, DEFAULT_MARGIN_SIZE)
    }

    override suspend fun setMarginSize(newValue: Int) = withIO {
        settings.put(KEY_MARGIN_SIZE, newValue)
    }
}
