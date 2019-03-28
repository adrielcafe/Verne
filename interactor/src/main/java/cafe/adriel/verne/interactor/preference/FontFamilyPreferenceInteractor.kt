package cafe.adriel.verne.interactor.preference

import cafe.adriel.verne.domain.repository.SettingsRepository
import cafe.adriel.verne.interactor.GetSetInteractor

class FontFamilyPreferenceInteractor(private val settingsRepository: SettingsRepository) : GetSetInteractor<String> {

    override suspend fun get() = settingsRepository.getFontFamily().toUpperCase()

    override suspend fun set(newValue: String) = settingsRepository.setFontFamily(newValue.toUpperCase())

}