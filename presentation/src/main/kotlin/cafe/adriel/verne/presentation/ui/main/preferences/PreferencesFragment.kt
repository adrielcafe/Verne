package cafe.adriel.verne.presentation.ui.main.preferences

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceGroup
import androidx.preference.PreferenceScreen
import androidx.preference.forEach
import androidx.recyclerview.widget.RecyclerView
import cafe.adriel.verne.presentation.BuildConfig
import cafe.adriel.verne.presentation.R
import cafe.adriel.verne.presentation.extension.colorFromAttr
import cafe.adriel.verne.presentation.extension.long
import cafe.adriel.verne.presentation.extension.openInChromeTab
import cafe.adriel.verne.presentation.extension.openInExternalBrowser
import cafe.adriel.verne.presentation.extension.share
import cafe.adriel.verne.presentation.extension.showSnackBar
import cafe.adriel.verne.presentation.helper.AnalyticsHelper
import cafe.adriel.verne.presentation.helper.CustomTabsHelper
import cafe.adriel.verne.shared.extension.launchMain
import com.instabug.bug.BugReporting
import kotlinx.coroutines.delay
import org.koin.android.ext.android.inject

class PreferencesFragment :
    PreferenceFragmentCompat(), Preference.OnPreferenceChangeListener, Preference.OnPreferenceClickListener {

    companion object {
        const val PREF_DARK_MODE = "appDarkMode"
        const val PREF_FULLSCREEN = "appFullscreen"

        const val ABOUT_CONTACT_US = "aboutContactUs"
        const val ABOUT_REPORT_BUG = "aboutReportBug"
        const val ABOUT_SHARE = "aboutShare"
        const val ABOUT_RATE_REVIEW = "aboutRateReview"
        const val ABOUT_PRIVACY_POLICY = "aboutPrivacyPolicy"
    }

    private val analyticsHelper by inject<AnalyticsHelper>()
    private val customTabsHelper by inject<CustomTabsHelper>()

    private val actionDelayMs by lazy { long(R.integer.action_delay) }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)

        findPreference<Preference>(PREF_DARK_MODE)?.onPreferenceChangeListener = this
        findPreference<Preference>(PREF_FULLSCREEN)?.onPreferenceChangeListener = this

        findPreference<Preference>(ABOUT_CONTACT_US)?.onPreferenceClickListener = this
        findPreference<Preference>(ABOUT_REPORT_BUG)?.onPreferenceClickListener = this
        findPreference<Preference>(ABOUT_SHARE)?.onPreferenceClickListener = this
        findPreference<Preference>(ABOUT_RATE_REVIEW)?.onPreferenceClickListener = this
        findPreference<Preference>(ABOUT_PRIVACY_POLICY)?.onPreferenceClickListener = this
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<RecyclerView>(R.id.recycler_view).isVerticalScrollBarEnabled = false
    }

    override fun setPreferenceScreen(preferenceScreen: PreferenceScreen?) {
        if (preferenceScreen != null) updatePreferenceIcon(preferenceScreen)
        super.setPreferenceScreen(preferenceScreen)
    }

    override fun onPreferenceChange(preference: Preference?, newValue: Any?): Boolean {
        when (preference?.key) {
            PREF_DARK_MODE -> if (newValue is Boolean) {
                analyticsHelper.logSwitchDarkMode(newValue)
            }
            PREF_FULLSCREEN -> if (newValue is Boolean) {
                analyticsHelper.logSwitchFullScreen(newValue)
            }
        }
        launchMain {
            delay(actionDelayMs)
            activity?.recreate()
        }
        return true
    }

    override fun onPreferenceClick(preference: Preference?) = when (preference?.key) {
        ABOUT_CONTACT_US -> {
            sendEmail()
            true
        }
        ABOUT_REPORT_BUG -> {
            BugReporting.show(BugReporting.ReportType.BUG)
            true
        }
        ABOUT_SHARE -> {
            shareApp()
            true
        }
        ABOUT_RATE_REVIEW -> {
            rateApp()
            true
        }
        ABOUT_PRIVACY_POLICY -> {
            showPrivacyPolicy()
            true
        }
        else -> false
    }

    private fun sendEmail() {
        activity?.apply {
            try {
                val appName = getString(R.string.app_name)
                val versionName = BuildConfig.VERSION_NAME
                val versionCode = BuildConfig.VERSION_CODE
                val sdkInt = Build.VERSION.SDK_INT

                val email = Uri.parse("mailto:${BuildConfig.CONTACT_EMAIL}")
                val subject = "$appName for Android | v$versionName (Build $versionCode), SDK $sdkInt"
                Intent(Intent.ACTION_SENDTO, email).run {
                    putExtra(Intent.EXTRA_SUBJECT, subject)
                    startActivity(this)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                showSnackBar("Oops! No Email app found :/")
            }
        }
    }

    private fun shareApp() =
        "${getString(R.string.you_should_try)}\n${BuildConfig.PLAY_STORE_URL}".share(requireActivity())

    private fun rateApp() = activity?.apply {
        try {
            Uri.parse(BuildConfig.MARKET_URL).openInExternalBrowser(this)
        } catch (e: Exception) {
            Uri.parse(BuildConfig.PLAY_STORE_URL).openInChromeTab(this, customTabsHelper.packageNameToUse)
        }
    }

    private fun showPrivacyPolicy() = activity?.apply {
        Uri.parse(BuildConfig.PRIVACY_POLICY_URL).openInChromeTab(this, customTabsHelper.packageNameToUse)
    }

    private fun updatePreferenceIcon(preference: Preference) {
        preference.isIconSpaceReserved = false
        if (preference is PreferenceGroup) {
            preference.forEach { childPreference ->
                childPreference.icon?.setTint(colorFromAttr(android.R.attr.actionMenuTextColor))
                updatePreferenceIcon(childPreference)
            }
        }
    }
}
