package cafe.adriel.verne.shared.extension

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

fun launchMain(body: suspend CoroutineScope.() -> Unit) {
    GlobalScope.launch(Dispatchers.Main) {
        body()
    }
}

suspend fun <T> withIo(body: suspend CoroutineScope.() -> T) = withContext(Dispatchers.IO) {
    body()
}
