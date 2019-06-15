package cafe.adriel.verne

import android.app.Application
import android.os.Build
import android.os.StrictMode
import cafe.adriel.verne.data.di.DataComponent
import cafe.adriel.verne.di.AppComponent
import cafe.adriel.verne.domain.di.DomainComponent
import cafe.adriel.verne.presentation.di.PresentationComponent
import cafe.adriel.verne.presentation.extension.minSdk
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import timber.log.Timber

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        if (!BuildConfig.RELEASE) {
            Timber.plant(Timber.DebugTree())
            initStrictMode()
        }

        initModules()
    }

    private fun initModules() {
        val modules = AppComponent(applicationContext).getModules() +
                PresentationComponent(applicationContext).getModules() +
                DomainComponent().getModules() +
                DataComponent().getModules()
        startKoin {
            androidLogger(if (BuildConfig.RELEASE) Level.ERROR else Level.DEBUG)
            androidContext(applicationContext)
            modules(modules)
        }
    }

    private fun initStrictMode() {
        val threadPolicy = StrictMode.ThreadPolicy
            .Builder()
            .detectAll()
            .permitDiskReads()
            .permitDiskWrites()
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
