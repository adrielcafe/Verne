package cafe.adriel.verne

import android.app.Application
import android.os.Build
import android.os.StrictMode
import cafe.adriel.verne.data.di.DataComponent
import cafe.adriel.verne.di.AppComponent
import cafe.adriel.verne.domain.di.DomainComponent
import cafe.adriel.verne.presentation.di.PresentationComponent
import cafe.adriel.verne.presentation.extension.color
import cafe.adriel.verne.presentation.extension.minSdk
import cafe.adriel.verne.presentation.helper.PreferencesHelper
import com.github.ajalt.timberkt.Timber
import com.instabug.bug.BugReporting
import com.instabug.bug.invocation.Option
import com.instabug.library.Instabug
import com.instabug.library.InstabugColorTheme
import com.instabug.library.invocation.InstabugInvocationEvent
import com.instabug.library.ui.onboarding.WelcomeMessage
import com.squareup.leakcanary.LeakCanary
import org.koin.android.ext.android.get
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        if (LeakCanary.isInAnalyzerProcess(this)) return
        LeakCanary.install(this)

        if (!BuildConfig.RELEASE) {
            Timber.plant(Timber.DebugTree())
            initStrictMode()
        }

        initModules()
        initBugReporting()
    }

    private fun initModules() {
        val modules =
            AppComponent(applicationContext).getModules() +
                    PresentationComponent(applicationContext).getModules() +
                    DomainComponent().getModules() +
                    DataComponent().getModules()
        startKoin {
            androidLogger(if (BuildConfig.RELEASE) Level.ERROR else Level.DEBUG)
            androidContext(applicationContext)
            modules(modules)
        }
    }

    private fun initBugReporting() {
        val preferencesHelper = get<PreferencesHelper>()
        val theme = if (preferencesHelper.isDarkMode()) {
            InstabugColorTheme.InstabugColorThemeDark
        } else {
            InstabugColorTheme.InstabugColorThemeLight
        }
        Instabug.Builder(this, BuildConfig.INSTABUG_KEY)
            .setInvocationEvents(InstabugInvocationEvent.NONE)
            .build()
        Instabug.setColorTheme(theme)
        Instabug.setPrimaryColor(color(R.color.colorAccent))
        Instabug.setWelcomeMessageState(WelcomeMessage.State.DISABLED)
        BugReporting.setOptions(Option.EMAIL_FIELD_OPTIONAL)
    }

    private fun initStrictMode() {
        val threadPolicy = StrictMode.ThreadPolicy
            .Builder()
            .detectAll()
            .permitDiskReads()
            .permitDiskWrites() // Caused by Instabug
            .permitNetwork() // Caused by NumberPicker
            .also {
                // Caused by AztecText
                minSdk(Build.VERSION_CODES.M) { it.permitResourceMismatches() }
            }
            .penaltyLog()
            .penaltyDropBox()
            .penaltyDeath()
            .build()
        val vmPolicy = StrictMode.VmPolicy
            .Builder()
            .detectFileUriExposure()
            .also {
                minSdk(Build.VERSION_CODES.M) { it.detectCleartextNetwork() }
                minSdk(Build.VERSION_CODES.O) { it.detectContentUriWithoutPermission() }
            }
            .penaltyLog()
            .penaltyDropBox()
            .penaltyDeath()
            .build()
        StrictMode.setThreadPolicy(threadPolicy)
        StrictMode.setVmPolicy(vmPolicy)
    }
}
