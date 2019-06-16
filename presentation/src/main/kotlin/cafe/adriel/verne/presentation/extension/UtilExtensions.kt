package cafe.adriel.verne.presentation.extension

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.browser.customtabs.CustomTabsIntent
import cafe.adriel.verne.presentation.R

inline fun minSdk(sdk: Int, body: () -> Unit) {
    if (Build.VERSION.SDK_INT >= sdk) body()
}

fun Uri.openInChromeTab(activity: Activity, packageName: String?) {
    packageName?.let {
        CustomTabsIntent.Builder()
            .setShowTitle(true)
            .setToolbarColor(activity.color(R.color.colorPrimary))
            .setSecondaryToolbarColor(activity.color(R.color.colorPrimaryDark))
            .addDefaultShareMenuItem()
            .enableUrlBarHiding()
            .build()
            .also { it.intent.setPackage(packageName) }
            .launchUrl(activity, this)
    } ?: openInExternalBrowser(activity)
}

fun Uri.openInExternalBrowser(activity: Activity, showErrorMessage: Boolean = true) = try {
    activity.startActivity(Intent(Intent.ACTION_VIEW, this))
} catch (e: Exception) {
    if (showErrorMessage) {
        activity.showSnackBar(R.string.something_went_wrong)
    }
    e.printStackTrace()
}
