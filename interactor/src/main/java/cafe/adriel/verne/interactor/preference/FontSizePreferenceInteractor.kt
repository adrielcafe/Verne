package cafe.adriel.verne.interactor.preference

import cafe.adriel.verne.domain.repository.PreferencesRepository

class FontSizePreferenceInteractor(private val preferencesRepository: PreferencesRepository) :
    PreferenceInteractor<Int> {

    override suspend fun get() = preferencesRepository.getFontSize()

    override suspend fun set(newValue: Int) = preferencesRepository.setFontSize(newValue)

}