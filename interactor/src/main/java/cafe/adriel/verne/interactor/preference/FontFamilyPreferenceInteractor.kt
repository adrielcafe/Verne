package cafe.adriel.verne.interactor.preference

import cafe.adriel.verne.domain.repository.PreferencesRepository

class FontFamilyPreferenceInteractor(private val preferencesRepository: PreferencesRepository) :
    PreferenceInteractor<String> {

    override suspend fun get() = preferencesRepository.getFontFamily().toUpperCase()

    override suspend fun set(newValue: String) = preferencesRepository.setFontFamily(newValue.toUpperCase())

}