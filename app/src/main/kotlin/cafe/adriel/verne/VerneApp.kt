package cafe.adriel.verne

import android.app.Application
import cafe.adriel.verne.data.dataModule
import cafe.adriel.verne.domain.domainModule
import cafe.adriel.verne.shared.sharedModule
import cafe.adriel.verne.ui.helper.uiHelperModule
import cafe.adriel.verne.ui.presentation.uiPresentationModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class VerneApp : Application() {

    private val allModules = listOf(
        appModule,
        sharedModule,
        domainModule,
        dataModule,
        uiHelperModule,
        uiPresentationModule
    )

    override fun onCreate() {
        super.onCreate()

        startKoin {
            if (BuildConfig.RELEASE.not())
                androidLogger(Level.DEBUG)
            androidContext(this@VerneApp)
            modules(allModules)
        }
    }
}
