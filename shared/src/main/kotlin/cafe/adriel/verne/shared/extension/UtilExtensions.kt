package cafe.adriel.verne.shared.extension

inline fun <reified T : Any> tagOf(): String = T::class.java.simpleName

inline fun <reified T : Any> javaClass(): Class<T> = T::class.java

inline fun <T> tryOrThrow(t: Throwable, body: () -> T) =
    try {
        body()
    } catch (e: Exception) {
        e.printStackTrace()
        throw t
    }
