package cafe.adriel.verne.domain.interactor.settings

internal interface SettingsInteractor<T> {
    suspend fun get(): T

    suspend fun set(newValue: T)
}
