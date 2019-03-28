package cafe.adriel.verne.interactor

interface GetSetInteractor<T> {

    suspend fun get(): T

    suspend fun set(newValue: T): Boolean
}