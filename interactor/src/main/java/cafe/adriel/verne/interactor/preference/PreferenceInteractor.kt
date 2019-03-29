package cafe.adriel.verne.interactor.preference

interface PreferenceInteractor<T> {

    suspend fun get(): T

    suspend fun set(newValue: T)
}