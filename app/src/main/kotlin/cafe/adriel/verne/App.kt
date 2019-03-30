package cafe.adriel.verne

import android.app.Application
import android.os.Build
import android.os.StrictMode
import cafe.adriel.verne.data.di.DataComponent
import cafe.adriel.verne.di.AppComponent
import cafe.adriel.verne.interactor.di.InteractorComponent
import cafe.adriel.verne.presentation.di.PresentationComponent
import cafe.adriel.verne.presentation.extension.color
import cafe.adriel.verne.presentation.extension.isDarkMode
import cafe.adriel.verne.presentation.extension.minSdk
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
                    InteractorComponent().getModules() +
                    DataComponent().getModules()
        startKoin {
            androidLogger(if (BuildConfig.RELEASE) Level.ERROR else Level.DEBUG)
            androidContext(applicationContext)
            modules(modules)
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

    private fun initStrictMode() {
        StrictMode.setThreadPolicy(
            StrictMode.ThreadPolicy.Builder()
                .detectAll()
                .permitDiskReads()
                // FIXME Both caused by NumberPicker > UIGestureRecognizerDelegate > JaCoCo
                .permitDiskWrites()
                .permitNetwork()
                .also {
                    // FIXME Caused by AztecText, fix and send a PR when possible
                    minSdk(Build.VERSION_CODES.M) { it.permitResourceMismatches() }
                }
                .penaltyLog()
                .penaltyDropBox()
                .penaltyDeath()
                .build()
        )
    }
}
