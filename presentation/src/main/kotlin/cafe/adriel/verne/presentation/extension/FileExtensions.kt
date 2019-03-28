package cafe.adriel.verne.presentation.extension

import android.app.Activity
import android.content.ClipDescription
import android.content.Context
import android.net.Uri
import androidx.core.app.ShareCompat
import androidx.core.content.FileProvider
import androidx.core.net.toFile
import cafe.adriel.verne.presentation.BuildConfig
import java.io.File

fun File.share(activity: Activity) {
    val uri = FileProvider.getUriForFile(activity, BuildConfig.PROVIDER_AUTHORITY, this)
    ShareCompat.IntentBuilder
        .from(activity)
        .setStream(uri)
        .setType(ClipDescription.MIMETYPE_TEXT_HTML)
        .startChooser()
}

fun Uri.readText(context: Context) = when (scheme) {
    "file" -> toFile().readText()
    "content" -> context.contentResolver.openInputStream(this)?.run {
        val text = readBytes().toString(Charsets.UTF_8)
        close()
        text
    }
    else -> null
}