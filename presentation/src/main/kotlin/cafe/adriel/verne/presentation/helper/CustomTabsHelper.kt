package cafe.adriel.verne.presentation.helper

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri

// Based on https://github.com/saurabharora90/CustomTabs-Kotlin
internal class CustomTabsHelper(appContext: Context) {

    companion object {
        private const val STABLE_PACKAGE = "com.android.chrome"
        private const val BETA_PACKAGE = "com.chrome.beta"
        private const val DEV_PACKAGE = "com.chrome.dev"
        private const val LOCAL_PACKAGE = "com.google.android.apps.chrome"
        private const val ACTION_CUSTOM_TABS_CONNECTION = "android.support.customtabs.action.CustomTabsService"
    }

    val packageNameToUse by lazy { getPackageNameToUse(appContext) }

    private fun getPackageNameToUse(context: Context): String? {
        val packageManager = context.packageManager
        val activityIntent = Intent(Intent.ACTION_VIEW, Uri.parse("http://www.example.com"))
        val packagesSupportingCustomTabs = getPackagesSupportingCustomTabs(activityIntent, packageManager)
        val defaultViewHandlerInfo = packageManager.resolveActivity(activityIntent, 0)
        val defaultViewHandlerPackageName = defaultViewHandlerInfo?.activityInfo?.packageName

        return findBestPackageName(
            packagesSupportingCustomTabs,
            defaultViewHandlerPackageName,
            hasSpecializedHandlerIntents(context, activityIntent)
        )
    }

    private fun getPackagesSupportingCustomTabs(intent: Intent, packageManager: PackageManager): List<String> {
        val resolvedActivityList = packageManager.queryIntentActivities(intent, 0)
        val packages = mutableListOf<String>()
        resolvedActivityList?.forEach { info ->
            val serviceIntent = Intent()
            serviceIntent.action = ACTION_CUSTOM_TABS_CONNECTION
            serviceIntent.setPackage(info.activityInfo.packageName)
            if (packageManager.resolveService(serviceIntent, 0) != null) {
                packages.add(info.activityInfo.packageName)
            }
        }
        return packages
    }

    private fun findBestPackageName(packages: List<String>, defaultPackage: String?, hasSpecializedIntent: Boolean) =
        when {
            packages.size == 1 -> packages[0]
            !defaultPackage.isNullOrBlank() &&
                    !hasSpecializedIntent &&
                    packages.contains(defaultPackage) -> defaultPackage
            packages.contains(STABLE_PACKAGE) -> STABLE_PACKAGE
            packages.contains(BETA_PACKAGE) -> BETA_PACKAGE
            packages.contains(DEV_PACKAGE) -> DEV_PACKAGE
            packages.contains(LOCAL_PACKAGE) -> LOCAL_PACKAGE
            else -> null
        }

    private fun hasSpecializedHandlerIntents(context: Context, intent: Intent): Boolean {
        try {
            val handlers = context.packageManager
                .queryIntentActivities(intent, PackageManager.GET_RESOLVED_FILTER)
            handlers?.forEach { info ->
                if (info.activityInfo != null &&
                    info.filter?.countDataAuthorities() != 0 &&
                    info.filter?.countDataPaths() != 0
                ) {
                    return true
                }
            }
        } catch (e: RuntimeException) {
            e.printStackTrace()
        }

        return false
    }
}
