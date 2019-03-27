package cafe.adriel.verne.presentation.extension

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.widget.Toast
import androidx.browser.customtabs.CustomTabsIntent
import cafe.adriel.verne.presentation.R
import cafe.adriel.verne.presentation.util.CustomTabsHelper

inline fun minSdk(sdk: Int, body: () -> Unit) {
    if (Build.VERSION.SDK_INT >= sdk) body()
}

fun Uri.openInChromeTab(context: Context) {
    val packageName = CustomTabsHelper.getPackageNameToUse(context)
    val intent = CustomTabsIntent.Builder()
        .setShowTitle(true)
        .setToolbarColor(context.color(R.color.colorPrimary))
        .setSecondaryToolbarColor(context.color(R.color.colorPrimaryDark))
        .addDefaultShareMenuItem()
        .enableUrlBarHiding()
        .build()

    if (packageName == null) {
        openInExternalBrowser(context)
    } else {
        intent.intent.setPackage(packageName)
        intent.launchUrl(context, this)
    }
}

fun Uri.openInExternalBrowser(context: Context, showErrorMessage: Boolean = true) = try {
    context.startActivity(Intent(Intent.ACTION_VIEW, this))
} catch (e: Exception) {
    if (showErrorMessage) {
        Toast.makeText(context, context.getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show()
    }
    e.printStackTrace()
}
