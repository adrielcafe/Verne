package cafe.adriel.verne.domain.interactor.settings

import cafe.adriel.verne.domain.repository.SettingsRepository

class FontFamilySettingsInteractor(
    private val settingsRepository: SettingsRepository
) : SettingsInteractor<String> {

    override suspend fun get() = settingsRepository.getFontFamily().toUpperCase()

    override suspend fun set(newValue: String) {
        if (!get().contentEquals(newValue)) {
            settingsRepository.setFontFamily(newValue.toUpperCase())
        }
    }
}
