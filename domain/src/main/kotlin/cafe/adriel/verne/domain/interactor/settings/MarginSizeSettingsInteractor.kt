package cafe.adriel.verne.domain.interactor.settings

import cafe.adriel.verne.domain.repository.SettingsRepository
import cafe.adriel.verne.shared.extension.withDefault

class MarginSizeSettingsInteractor(
    private val settingsRepository: SettingsRepository
) : SettingsInteractor<Int> {

    override suspend fun get() = withDefault {
        settingsRepository.getMarginSize()
    }

    override suspend fun set(newValue: Int) = withDefault {
        if (get() != newValue) {
            settingsRepository.setMarginSize(newValue)
        }
    }
}
