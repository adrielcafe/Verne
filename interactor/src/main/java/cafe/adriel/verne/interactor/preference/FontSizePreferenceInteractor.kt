package cafe.adriel.verne.interactor.preference

import cafe.adriel.verne.domain.repository.SettingsRepository
import cafe.adriel.verne.interactor.GetSetInteractor

class FontSizePreferenceInteractor(private val settingsRepository: SettingsRepository) : GetSetInteractor<Int> {

    override suspend fun get() = settingsRepository.getFontSize()

    override suspend fun set(newValue: Int) = settingsRepository.setFontSize(newValue)

}