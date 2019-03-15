package cafe.adriel.verne.extension

import android.content.Context
import cafe.adriel.verne.R
import org.ocpsoft.prettytime.PrettyTime
import java.text.SimpleDateFormat
import java.util.*

private val dateFormatterShort by lazy {
    SimpleDateFormat.getDateTimeInstance(SimpleDateFormat.SHORT, SimpleDateFormat.MEDIUM)
}
private val dateFormatterMedium by lazy {
    SimpleDateFormat.getDateTimeInstance(SimpleDateFormat.MEDIUM, SimpleDateFormat.SHORT)
}
private val prettyTime by lazy { PrettyTime() }

fun Date.formatShort(): String = dateFormatterShort.format(this)

fun Date.formatMedium(): String = dateFormatterMedium.format(this)

fun Int.formatSeconds(context: Context): String = when {
    this < 60 -> {
        context.resources.getQuantityString(R.plurals.seconds, this, this)
    }
    this == 60 -> {
        context.resources.getQuantityString(R.plurals.minutes, 1, 1)
    }
    else -> {
        val date = Calendar.getInstance().apply {
            add(Calendar.SECOND, this@formatSeconds)
        }
        prettyTime.formatDuration(date)
    }
}