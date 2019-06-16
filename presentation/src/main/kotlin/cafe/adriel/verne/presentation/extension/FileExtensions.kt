package cafe.adriel.verne.presentation.extension

import android.app.Activity
import android.content.ClipDescription
import android.content.Context
import android.net.Uri
import androidx.core.app.ShareCompat
import androidx.core.content.FileProvider
import androidx.core.net.toFile
import cafe.adriel.verne.presentation.BuildConfig
import cafe.adriel.verne.presentation.R
import java.io.File

fun File.share(activity: Activity) = try {
    val uri = FileProvider.getUriForFile(activity, BuildConfig.PROVIDER_AUTHORITY, this)
    ShareCompat.IntentBuilder
        .from(activity)
        .setStream(uri)
        .setType(ClipDescription.MIMETYPE_TEXT_HTML)
        .startChooser()
} catch (e: Exception) {
    e.printStackTrace()
    activity.showSnackBar(R.string.something_went_wrong)
}

fun Uri.readText(context: Context) = when (scheme) {
    "file" -> toFile().readText()
    "content" -> context.contentResolver.openInputStream(this)?.use { stream ->
        stream.readBytes().toString(Charsets.UTF_8)
    }
    else -> null
}
