package cafe.adriel.verne.data.repository

import android.content.SharedPreferences
import androidx.core.content.edit
import cafe.adriel.verne.domain.repository.PreferencesRepository
import cafe.adriel.verne.domain.repository.PreferencesRepository.Companion.DEFAULT_FONT_FAMILY
import cafe.adriel.verne.domain.repository.PreferencesRepository.Companion.DEFAULT_FONT_SIZE
import cafe.adriel.verne.domain.repository.PreferencesRepository.Companion.DEFAULT_MARGIN_SIZE
import cafe.adriel.verne.shared.extension.withIo

class AppPreferencesRepository(private val preferences: SharedPreferences) : PreferencesRepository {

    companion object {
        private const val PREF_FONT_FAMILY = "editorFontFamily"
        private const val PREF_FONT_SIZE = "editorFontSize"
        private const val PREF_MARGIN_SIZE = "editorMarginSize"
    }

    override suspend fun getFontFamily() = withIo {
        preferences.getString(PREF_FONT_FAMILY, DEFAULT_FONT_FAMILY) ?: DEFAULT_FONT_FAMILY
    }

    override suspend fun setFontFamily(newValue: String) = withIo {
        preferences.edit { putString(PREF_FONT_FAMILY, newValue) }
    }

    override suspend fun getFontSize() = withIo {
        preferences.getInt(PREF_FONT_SIZE, DEFAULT_FONT_SIZE)
    }

    override suspend fun setFontSize(newValue: Int) = withIo {
        preferences.edit { putInt(PREF_FONT_SIZE, newValue) }
    }

    override suspend fun getMarginSize() = withIo {
        preferences.getInt(PREF_MARGIN_SIZE, DEFAULT_MARGIN_SIZE)
    }

    override suspend fun setMarginSize(newValue: Int) = withIo {
        preferences.edit { putInt(PREF_MARGIN_SIZE, newValue) }
    }
}
