package cafe.adriel.verne.data.repository

import cafe.adriel.verne.domain.proto.Settings
import cafe.adriel.verne.domain.repository.SettingsRepository
import cafe.adriel.verne.domain.repository.SettingsRepository.Companion.DEFAULT_FONT_FAMILY
import cafe.adriel.verne.domain.repository.SettingsRepository.Companion.DEFAULT_FONT_SIZE
import cafe.adriel.verne.domain.repository.SettingsRepository.Companion.DEFAULT_MARGIN_SIZE
import cafe.adriel.verne.shared.extension.withIo
import cafe.adriel.verne.shared.model.AppConfig
import java.io.IOException

class AppSettingsRepository(private val appConfig: AppConfig) : SettingsRepository {

    private lateinit var settings: Settings

    override suspend fun getFontFamily(): String = withIo {
        getSettings().editorFontFamily
    }

    override suspend fun setFontFamily(newValue: String) {
        editSetting { editorFontFamily = newValue }
    }

    override suspend fun getFontSize() = withIo {
        getSettings().editorFontSize
    }

    override suspend fun setFontSize(newValue: Int) {
        editSetting { editorFontSize = newValue }
    }

    override suspend fun getMarginSize() = withIo {
        getSettings().editorMarginSize
    }

    override suspend fun setMarginSize(newValue: Int) {
        editSetting { editorMarginSize = newValue }
    }

    private suspend fun getSettings() = withIo {
        if (!::settings.isInitialized) {
            settings = loadSettings()
        }
        settings
    }

    private suspend fun getDefaultSettings() = withIo {
        Settings.newBuilder()
            .setEditorFontFamily(DEFAULT_FONT_FAMILY)
            .setEditorFontSize(DEFAULT_FONT_SIZE)
            .setEditorMarginSize(DEFAULT_MARGIN_SIZE)
            .build()
    }

    private suspend fun loadSettings() = withIo {
        if (appConfig.settingsFile.exists()) {
            try {
                Settings.parseFrom(appConfig.settingsFile.inputStream())
            } catch (e: IOException) {
                e.printStackTrace()
                getDefaultSettings()
            }
        } else {
            getDefaultSettings()
        }
    }

    private suspend fun editSetting(body: Settings.Builder.() -> Unit) = withIo {
        settings = Settings.newBuilder(settings).run {
            body(this)
            build()
        }
        settings.writeTo(appConfig.settingsFile.outputStream())
    }
}
