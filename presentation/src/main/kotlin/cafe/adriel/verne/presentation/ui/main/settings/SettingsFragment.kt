package cafe.adriel.verne.presentation.ui.main.settings

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
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
import cafe.adriel.verne.presentation.util.AnalyticsUtil
import com.instabug.bug.BugReporting
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SettingsFragment : PreferenceFragmentCompat(), Preference.OnPreferenceChangeListener, Preference.OnPreferenceClickListener {

    companion object {
        const val APP_DARK_MODE = "appDarkMode"
        const val APP_FULLSCREEN = "appFullscreen"

        const val ABOUT_CONTACT_US = "aboutContactUs"
        const val ABOUT_REPORT_BUG = "aboutReportBug"
        const val ABOUT_SHARE = "aboutShare"
        const val ABOUT_RATE_REVIEW = "aboutRateReview"
        const val ABOUT_PRIVACY_POLICY = "aboutPrivacyPolicy"
    }

    private val actionDelayMs by lazy { long(R.integer.action_delay) }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)

        findPreference<Preference>(APP_DARK_MODE)?.onPreferenceChangeListener = this
        findPreference<Preference>(APP_FULLSCREEN)?.onPreferenceChangeListener = this

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
        GlobalScope.launch(Dispatchers.Main) {
            when (preference?.key) {
                APP_DARK_MODE -> if (newValue is Boolean) {
                    AnalyticsUtil.logSwitchDarkMode(newValue)
                }
                APP_FULLSCREEN -> if (newValue is Boolean) {
                    AnalyticsUtil.logSwitchFullScreen(newValue)
                }
            }
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
        try {
            val email = Uri.parse("mailto:${BuildConfig.CONTACT_EMAIL}")
            val subject = "${context?.getString(R.string.app_name)} for Android | v${BuildConfig.VERSION_NAME} (Build ${BuildConfig.VERSION_CODE}), SDK ${Build.VERSION.SDK_INT}"
            Intent(Intent.ACTION_SENDTO, email).run {
                putExtra(Intent.EXTRA_SUBJECT, subject)
                context?.startActivity(this)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(context, "Oops! No Email app found :/", Toast.LENGTH_LONG).show()
        }
    }

    private fun shareApp() = "${getString(R.string.you_should_try)}\n${BuildConfig.PLAY_STORE_URL}".share(requireActivity())

    private fun rateApp() = context?.apply {
        try {
            Uri.parse(BuildConfig.MARKET_URL).openInExternalBrowser(this)
        } catch (e: Exception) {
            Uri.parse(BuildConfig.PLAY_STORE_URL).openInChromeTab(this)
        }
    }

    private fun showPrivacyPolicy() = context?.apply {
        Uri.parse(BuildConfig.PRIVACY_POLICY_URL).openInChromeTab(this)
    }

    private fun updatePreferenceIcon(preference: Preference) {
        preference.isIconSpaceReserved = false
        if (preference is PreferenceGroup) {
            preference.forEach {
                it.icon?.setTint(colorFromAttr(android.R.attr.actionMenuTextColor))
                updatePreferenceIcon(it)
            }
        }
    }
}
