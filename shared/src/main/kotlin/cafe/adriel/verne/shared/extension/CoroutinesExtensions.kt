package cafe.adriel.verne.shared.extension

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

suspend fun <T> withIO(body: suspend () -> T) =
    withContext(Dispatchers.IO) { body() }

suspend fun <T> withDefault(body: suspend () -> T) =
    withContext(Dispatchers.Default) { body() }
