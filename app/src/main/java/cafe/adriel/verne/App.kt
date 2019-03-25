package cafe.adriel.verne

import android.app.Application
import android.os.Build
import android.os.StrictMode
import cafe.adriel.verne.di.AppComponent
import cafe.adriel.verne.extension.color
import cafe.adriel.verne.extension.debug
import cafe.adriel.verne.extension.isDarkMode
import cafe.adriel.verne.extension.minSdk
import cafe.adriel.verne.util.AnalyticsUtil
import com.github.ajalt.timberkt.Timber
import com.instabug.bug.BugReporting
import com.instabug.bug.invocation.Option
import com.instabug.library.Instabug
import com.instabug.library.InstabugColorTheme
import com.instabug.library.invocation.InstabugInvocationEvent
import com.instabug.library.ui.onboarding.WelcomeMessage
import com.squareup.leakcanary.LeakCanary
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

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
                    // TODO Caused by NumberPicker > UIGestureRecognizerDelegate > JaCoCo
                    .permitDiskWrites()
                    .permitNetwork()
                    .also {
                        minSdk(Build.VERSION_CODES.M){
                            // TODO Caused by AztecText, fix and send a PR when possible
                            it.permitResourceMismatches()
                        }
                    }
                    .penaltyLog()
                    .penaltyDropBox()
                    .penaltyDeath()
                    .build()
            )
        }

        AnalyticsUtil.init(this)

        initModules()
        initBugReporting()
    }

    private fun initModules() {
        startKoin {
            androidLogger(if (BuildConfig.RELEASE) Level.ERROR else Level.DEBUG)
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

}
