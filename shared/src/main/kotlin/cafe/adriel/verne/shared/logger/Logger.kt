package cafe.adriel.verne.shared.logger

interface Logger {

    fun d(message: String)

    fun w(message: String, exception: Throwable? = null)

    fun e(message: String, exception: Throwable)
}
