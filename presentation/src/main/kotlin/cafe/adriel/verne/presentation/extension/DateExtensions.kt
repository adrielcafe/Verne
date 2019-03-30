package cafe.adriel.verne.presentation.extension

import android.content.Context
import cafe.adriel.verne.presentation.R
import org.ocpsoft.prettytime.PrettyTime
import java.util.Calendar

const val SIXTY_SECONDS = 60

private val prettyTime by lazy { PrettyTime() }

fun Int.formatSeconds(context: Context): String = when {
    this < SIXTY_SECONDS -> {
        context.resources.getQuantityString(R.plurals.seconds, this, this)
    }
    this == SIXTY_SECONDS -> {
        context.resources.getQuantityString(R.plurals.minutes, 1, 1)
    }
    else -> {
        val date = Calendar.getInstance().apply {
            add(Calendar.SECOND, this@formatSeconds)
        }
        prettyTime.formatDuration(date)
    }
}
