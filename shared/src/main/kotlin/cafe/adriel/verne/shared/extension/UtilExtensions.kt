package cafe.adriel.verne.shared.extension

var isDebug = false

inline fun debug(body: () -> Unit) {
    if (isDebug) body()
}

inline fun <reified T : Any> tagOf(): String = T::class.java.simpleName

inline fun <reified T : Any> javaClass(): Class<T> = T::class.java
