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
import cafe.adriel.verne.presentation.util.AnalyticsUtil
import cafe.adriel.verne.shared.extension.debug
import cafe.adriel.verne.shared.extension.isDebug
import com.github.ajalt.timberkt.Timber
import com.instabug.bug.BugReporting
import com.instabug.bug.invocation.Option
import com.instabug.library.Instabug
import com.instabug.library.InstabugColorTheme
import com.instabug.library.invocation.InstabugInvocationEvent
import com.instabug.library.ui.onboarding.WelcomeMessage
import com.squareup.leakcanary.LeakCanary
import com.tencent.mmkv.MMKV
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class App : Application() {

    companion object {
        const val BASE_DIR_NAME = "${BuildConfig.APPLICATION_ID}.db"
    }

    override fun onCreate() {
        super.onCreate()
        isDebug = BuildConfig.DEBUG

        if (LeakCanary.isInAnalyzerProcess(this)) return
        LeakCanary.install(this)

        debug {
            Timber.plant(Timber.DebugTree())

            StrictMode.setThreadPolicy(
                StrictMode.ThreadPolicy.Builder()
                    .detectAll()
                    .permitDiskReads()
                    // FIXME Caused by NumberPicker > UIGestureRecognizerDelegate > JaCoCo
                    .permitDiskWrites()
                    .permitNetwork()
                    .also {
                        minSdk(Build.VERSION_CODES.M){
                            // FIXME Caused by AztecText, fix and send a PR when possible
                            it.permitResourceMismatches()
                        }
                    }
                    .penaltyLog()
                    .penaltyDropBox()
                    .penaltyDeath()
                    .build()
            )
        }

        MMKV.initialize(this)
        AnalyticsUtil.init(this)

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

}
