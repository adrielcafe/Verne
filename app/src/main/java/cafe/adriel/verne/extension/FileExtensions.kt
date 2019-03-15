package cafe.adriel.verne.extension

import android.app.Activity
import android.content.ClipDescription
import android.content.Context
import android.net.Uri
import androidx.core.app.ShareCompat
import androidx.core.content.FileProvider
import androidx.core.net.toFile
import cafe.adriel.verne.App
import cafe.adriel.verne.model.ExplorerItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileFilter

private val fileFilter by lazy {
    FileFilter { !it.isDirectory && !it.isHidden }
}

fun File.asExplorerItem(): ExplorerItem = if(isDirectory) ExplorerItem.Folder(path) else ExplorerItem.File(path)

suspend fun File.filesCount() = withContext(Dispatchers.IO){
    walk().fold(0) { acc, file ->
        acc + (file.listFiles(fileFilter)?.size ?: 0)
    }
}

fun File.share(activity: Activity) {
    val uri = FileProvider.getUriForFile(activity, App.PROVIDER_AUTHORITY, this)
    ShareCompat.IntentBuilder
        .from(activity)
        .setStream(uri)
        .setType(ClipDescription.MIMETYPE_TEXT_HTML)
        .startChooser()
}

fun Uri.readText(context: Context) = when(scheme){
    "file" -> toFile().readText()
    "content" -> context.contentResolver.openInputStream(this)?.run {
        val text = readBytes().toString(Charsets.UTF_8)
        close()
        text
    }
    else -> null
}