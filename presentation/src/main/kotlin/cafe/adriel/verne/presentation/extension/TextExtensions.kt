package cafe.adriel.verne.presentation.extension

import android.app.Activity
import android.content.ClipDescription
import androidx.core.app.ShareCompat
import androidx.core.text.HtmlCompat
import androidx.core.text.getSpans
import androidx.core.text.toSpanned
import cafe.adriel.verne.shared.extension.withDefault
import org.wordpress.aztec.Constants
import java.util.StringTokenizer
import kotlin.math.roundToInt

private const val WORDS_PER_MINUTE = 200F

suspend fun String.paragraphCount() = withDefault {
    trim()
        .split('\n')
        .filter { it.isNotBlank() && it.trim() != Constants.ZWJ_STRING }
        .size
}

suspend fun String.wordCount() = withDefault {
    StringTokenizer(replace(Constants.ZWJ_STRING, "")).countTokens()
}

suspend fun String.charCount() = withDefault {
    val tokenizer = StringTokenizer(replace(Constants.ZWJ_STRING, ""))
    var count = 0
    while (tokenizer.hasMoreTokens()) {
        count += tokenizer.nextToken().length
    }
    count
}

suspend fun String.readTimeInSeconds() = withDefault {
    val seconds = wordCount() / WORDS_PER_MINUTE * SIXTY_SECONDS
    seconds.roundToInt()
}

suspend fun String.fromHtml() = withDefault {
    HtmlCompat.fromHtml(this@fromHtml, HtmlCompat.FROM_HTML_MODE_COMPACT)
}

fun String.share(activity: Activity, html: Boolean = false) =
    ShareCompat.IntentBuilder.from(activity).run {
        if (html) {
            setHtmlText(this@share)
            setType(ClipDescription.MIMETYPE_TEXT_HTML)
        } else {
            setText(this@share)
            setType(ClipDescription.MIMETYPE_TEXT_PLAIN)
        }
        startChooser()
    }

fun CharSequence.hasSpans() = toSpanned().getSpans<Any>().isNotEmpty()
