package cafe.adriel.verne.presentation.helper

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.text.TextUtils

// Based on https://github.com/saurabharora90/CustomTabs-Kotlin
class CustomTabsHelper(appContext: Context) {

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
        val defaultViewHandlerInfo = packageManager.resolveActivity(activityIntent, 0)
        var defaultViewHandlerPackageName: String? = null
        if (defaultViewHandlerInfo != null) {
            defaultViewHandlerPackageName = defaultViewHandlerInfo.activityInfo.packageName
        }

        val resolvedActivityList = packageManager.queryIntentActivities(activityIntent, 0)
        val packagesSupportingCustomTabs = ArrayList<String>()
        for (info in resolvedActivityList) {
            val serviceIntent = Intent()
            serviceIntent.action = ACTION_CUSTOM_TABS_CONNECTION
            serviceIntent.setPackage(info.activityInfo.packageName)
            if (packageManager.resolveService(serviceIntent, 0) != null) {
                packagesSupportingCustomTabs.add(info.activityInfo.packageName)
            }
        }

        return if (packagesSupportingCustomTabs.size == 1) {
            packagesSupportingCustomTabs[0]
        } else if (!TextUtils.isEmpty(defaultViewHandlerPackageName) &&
            !hasSpecializedHandlerIntents(
                context,
                activityIntent
            ) &&
            packagesSupportingCustomTabs.contains(defaultViewHandlerPackageName)) {
            defaultViewHandlerPackageName
        } else if (packagesSupportingCustomTabs.contains(STABLE_PACKAGE)) {
            STABLE_PACKAGE
        } else if (packagesSupportingCustomTabs.contains(BETA_PACKAGE)) {
            BETA_PACKAGE
        } else if (packagesSupportingCustomTabs.contains(DEV_PACKAGE)) {
            DEV_PACKAGE
        } else if (packagesSupportingCustomTabs.contains(LOCAL_PACKAGE)) {
            LOCAL_PACKAGE
        } else {
            null
        }
    }

    private fun hasSpecializedHandlerIntents(context: Context, intent: Intent): Boolean {
        try {
            val pm = context.packageManager
            val handlers = pm.queryIntentActivities(
                intent,
                PackageManager.GET_RESOLVED_FILTER)
            if (handlers == null || handlers.size == 0) {
                return false
            }
            for (resolveInfo in handlers) {
                val filter = resolveInfo.filter ?: continue
                if (filter.countDataAuthorities() == 0 || filter.countDataPaths() == 0) continue
                if (resolveInfo.activityInfo == null) continue
                return true
            }
        } catch (e: RuntimeException) {
            e.printStackTrace()
        }

        return false
    }
}
