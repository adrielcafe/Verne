package cafe.adriel.verne.domain.interactor.settings

import cafe.adriel.verne.domain.repository.SettingsRepository

class MarginSizeSettingsInteractor(
    private val settingsRepository: SettingsRepository
) : SettingsInteractor<Int> {

    override suspend fun get() = settingsRepository.getMarginSize()

    override suspend fun set(newValue: Int) {
        if (get() != newValue) {
            settingsRepository.setMarginSize(newValue)
        }
    }
}
