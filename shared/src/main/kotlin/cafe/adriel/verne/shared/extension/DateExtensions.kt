package cafe.adriel.verne.shared.extension

import java.text.SimpleDateFormat
import java.util.Date

private val dateFormatterShort by lazy {
    SimpleDateFormat.getDateTimeInstance(SimpleDateFormat.SHORT, SimpleDateFormat.MEDIUM)
}
private val dateFormatterMedium by lazy {
    SimpleDateFormat.getDateTimeInstance(SimpleDateFormat.MEDIUM, SimpleDateFormat.SHORT)
}

fun Date.formatShort(): String = dateFormatterShort.format(this)

fun Date.formatMedium(): String = dateFormatterMedium.format(this)