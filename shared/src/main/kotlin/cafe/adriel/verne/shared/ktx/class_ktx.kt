package cafe.adriel.verne.shared.ktx

inline fun <reified T : Any> tagOf(): String = T::class.java.simpleName

inline fun <reified T : Any> javaClass(): Class<T> = T::class.java
