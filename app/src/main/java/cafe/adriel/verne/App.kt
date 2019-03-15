package cafe.adriel.verne

import android.app.Application
import android.os.StrictMode
import androidx.core.content.edit
import androidx.preference.PreferenceManager
import cafe.adriel.verne.di.AppComponent
import cafe.adriel.verne.extension.color
import cafe.adriel.verne.extension.debug
import cafe.adriel.verne.extension.isDarkMode
import cafe.adriel.verne.util.AnalyticsUtil
import cafe.adriel.verne.view.main.settings.SettingsFragment
import com.franmontiel.localechanger.LocaleChanger
import com.github.ajalt.timberkt.Timber
import com.instabug.bug.BugReporting
import com.instabug.bug.invocation.Option
import com.instabug.library.Instabug
import com.instabug.library.InstabugColorTheme
import com.instabug.library.invocation.InstabugInvocationEvent
import com.instabug.library.ui.onboarding.WelcomeMessage
import com.squareup.leakcanary.LeakCanary
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import java.util.Locale

class App : Application() {

    companion object {
        const val BASE_DIR_NAME = "${BuildConfig.APPLICATION_ID}.db"
        const val PROVIDER_AUTHORITY = "${BuildConfig.APPLICATION_ID}.provider"

        const val CONTACT_EMAIL = "contact@verne.app"
        const val PRIVACY_POLICY_URL = "https://adriel.cafe/privacy_policy/verne"
        const val PLAY_STORE_URL = "https://play.google.com/store/apps/details?id=${BuildConfig.APPLICATION_ID}"
        const val MARKET_URL = "market://details?id=${BuildConfig.APPLICATION_ID}"
    }

    override fun onCreate() {
        super.onCreate()
        if (LeakCanary.isInAnalyzerProcess(this)) return
        LeakCanary.install(this)

        debug {
            Timber.plant(Timber.DebugTree())

            StrictMode.setThreadPolicy(
                StrictMode.ThreadPolicy.Builder()
                    .detectAll()
                    .permitDiskReads()
                    .penaltyLog()
                    .penaltyDropBox()
                    .penaltyDeath()
                    .build()
            )
        }

        AnalyticsUtil.init(this)

        initModules()
        initBugReporting()
        initLanguage()
    }

    private fun initModules() {
        startKoin {
            logger(if (BuildConfig.RELEASE) Level.ERROR else Level.DEBUG)
            androidContext(applicationContext)
            modules(AppComponent(applicationContext).getModules())
        }
    }

    private fun initBugReporting() {
        val theme = if (isDarkMode())
            InstabugColorTheme.InstabugColorThemeDark
        else
            InstabugColorTheme.InstabugColorThemeLight
        Instabug.Builder(this, BuildConfig.INSTABUG_KEY)
            .setInvocationEvents(InstabugInvocationEvent.NONE)
            .build()
        Instabug.setColorTheme(theme)
        Instabug.setPrimaryColor(color(R.color.colorAccent))
        Instabug.setWelcomeMessageState(WelcomeMessage.State.DISABLED)
        BugReporting.setOptions(Option.EMAIL_FIELD_OPTIONAL)
    }

    private fun initLanguage() {
        val supportedLanguages = resources
            .getStringArray(R.array.preference_language_values)
            .map { Locale(it) }
        LocaleChanger.initialize(applicationContext, supportedLanguages)

        val preferences = PreferenceManager.getDefaultSharedPreferences(this)
        if (!preferences.contains(SettingsFragment.APP_LANGUAGE)) {
            var currentLanguage = LocaleChanger.getLocale().language
            val languageSupported = resources
                .getStringArray(R.array.preference_language_values)
                .contains(currentLanguage)
            if (!languageSupported) {
                currentLanguage = Locale.ENGLISH.language
            }
            preferences.edit { putString(SettingsFragment.APP_LANGUAGE, currentLanguage) }
        }
    }
}
