package cafe.adriel.verne.interactor.preference

import cafe.adriel.verne.domain.repository.PreferencesRepository

class MarginSizePreferenceInteractor(private val preferencesRepository: PreferencesRepository) :
    PreferenceInteractor<Int> {

    override suspend fun get() = preferencesRepository.getMarginSize()

    override suspend fun set(newValue: Int) = preferencesRepository.setMarginSize(newValue)

}