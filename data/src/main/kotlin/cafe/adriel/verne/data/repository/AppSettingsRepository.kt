package cafe.adriel.verne.data.repository

import cafe.adriel.verne.domain.repository.SettingsRepository
import cafe.adriel.verne.domain.repository.SettingsRepository.Companion.DEFAULT_FONT_FAMILY
import cafe.adriel.verne.domain.repository.SettingsRepository.Companion.DEFAULT_FONT_SIZE
import cafe.adriel.verne.domain.repository.SettingsRepository.Companion.DEFAULT_MARGIN_SIZE
import cafe.adriel.verne.shared.extension.withIo
import com.tencent.mmkv.MMKV

class AppSettingsRepository(private val settings: MMKV) : SettingsRepository {

    companion object {
        private const val PREF_FONT_FAMILY = "editorFontFamily"
        private const val PREF_FONT_SIZE = "editorFontSize"
        private const val PREF_MARGIN_SIZE = "editorMarginSize"
    }

    override suspend fun getFontFamily() = withIo {
        settings.decodeString(PREF_FONT_FAMILY, DEFAULT_FONT_FAMILY) ?: DEFAULT_FONT_FAMILY
    }

    override suspend fun setFontFamily(newValue: String) = withIo {
        settings.encode(PREF_FONT_FAMILY, newValue)
    }

    override suspend fun getFontSize() = withIo {
        settings.decodeInt(PREF_FONT_SIZE, DEFAULT_FONT_SIZE)
    }

    override suspend fun setFontSize(newValue: Int) = withIo {
        settings.encode(PREF_FONT_SIZE, newValue)
    }

    override suspend fun getMarginSize() = withIo {
        settings.decodeInt(PREF_MARGIN_SIZE, DEFAULT_MARGIN_SIZE)
    }

    override suspend fun setMarginSize(newValue: Int) = withIo {
        settings.encode(PREF_MARGIN_SIZE, newValue)
    }
}
