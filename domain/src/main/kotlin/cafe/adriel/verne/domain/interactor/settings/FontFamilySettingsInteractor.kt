package cafe.adriel.verne.domain.interactor.settings

import cafe.adriel.verne.domain.repository.SettingsRepository
import cafe.adriel.verne.shared.extension.withDefault

class FontFamilySettingsInteractor(
    private val settingsRepository: SettingsRepository
) : SettingsInteractor<String> {

    override suspend fun get() = withDefault {
        settingsRepository.getFontFamily().toUpperCase()
    }

    override suspend fun set(newValue: String) = withDefault {
        if (!get().contentEquals(newValue)) {
            settingsRepository.setFontFamily(newValue.toUpperCase())
        }
    }
}
