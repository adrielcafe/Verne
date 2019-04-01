package cafe.adriel.verne.domain.interactor.settings

import cafe.adriel.verne.domain.repository.SettingsRepository

class FontSizeSettingsInteractor(
    private val settingsRepository: SettingsRepository
) : SettingsInteractor<Int> {

    override suspend fun get() = settingsRepository.getFontSize()

    override suspend fun set(newValue: Int) {
        if (get() != newValue) {
            settingsRepository.setFontSize(newValue)
        }
    }
}
