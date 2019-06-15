package cafe.adriel.verne.shared.extension

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

suspend fun <T> withIo(body: suspend CoroutineScope.() -> T) = withContext(Dispatchers.IO) {
    body()
}

suspend fun <T> withDefault(body: suspend CoroutineScope.() -> T) = withContext(Dispatchers.Default) {
    body()
}
