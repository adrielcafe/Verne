package cafe.adriel.verne.extension

import android.app.Activity
import android.content.ClipDescription
import androidx.core.app.ShareCompat
import androidx.core.text.HtmlCompat
import androidx.core.text.getSpans
import androidx.core.text.toSpanned
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.wordpress.aztec.Constants
import java.util.*
import kotlin.math.roundToInt

private const val WORDS_PER_MINUTE = 200F

suspend fun String.paragraphCount() = withContext(Dispatchers.Default){
    trim()
        .split('\n')
        .filter { it.isNotBlank() && it.trim() != Constants.ZWJ_STRING }
        .size
}

suspend fun String.wordCount() = withContext(Dispatchers.Default){
    val str = this@wordCount.replace(Constants.ZWJ_STRING, "")
    StringTokenizer(str).countTokens()
}

suspend fun String.charCount() = withContext(Dispatchers.Default){
    val str = this@charCount.replace(Constants.ZWJ_STRING, "")
    val tokenizer = StringTokenizer(str)
    var count = 0
    while(tokenizer.hasMoreTokens()){
        count += tokenizer.nextToken().length
    }
    count
}

suspend fun String.readTimeInSeconds() = withContext(Dispatchers.Default){
    val wordCount = this@readTimeInSeconds.wordCount()
    val seconds = (wordCount / WORDS_PER_MINUTE) * 60
    seconds.roundToInt()
}

fun String.fromHtml() = HtmlCompat.fromHtml(this, HtmlCompat.FROM_HTML_MODE_COMPACT)

fun String.share(activity: Activity, html: Boolean = false) =
    ShareCompat.IntentBuilder.from(activity).run {
        if(html){
            setHtmlText(this@share)
            setType(ClipDescription.MIMETYPE_TEXT_HTML)
        } else {
            setText(this@share)
            setType(ClipDescription.MIMETYPE_TEXT_PLAIN)
        }
        startChooser()
    }

fun CharSequence.hasSpans() = toSpanned().getSpans<Any>().isNotEmpty()