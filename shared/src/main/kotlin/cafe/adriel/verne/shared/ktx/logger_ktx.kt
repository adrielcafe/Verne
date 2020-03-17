package cafe.adriel.verne.shared.ktx

import cafe.adriel.verne.shared.logger.Logger

private const val CURRENT_CLASS_INDEX = 2

val Logger.currentClassName: String
    get() = Throwable().stackTrace[CURRENT_CLASS_INDEX].className
        .split(".").last()
        .split("$").first()
