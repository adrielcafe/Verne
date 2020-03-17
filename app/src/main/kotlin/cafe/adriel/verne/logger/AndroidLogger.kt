package cafe.adriel.verne.logger

import android.util.Log
import cafe.adriel.verne.shared.ktx.currentClassName
import cafe.adriel.verne.shared.logger.Logger

internal object AndroidLogger : Logger {

    override fun d(message: String) {
        Log.d("DEBUG [$currentClassName]", message)
    }

    override fun w(message: String, exception: Throwable?) {
        Log.w("WARNING [$currentClassName]", message, exception)
    }

    override fun e(message: String, exception: Throwable) {
        Log.e("ERROR [$currentClassName]", message, exception)
    }
}
