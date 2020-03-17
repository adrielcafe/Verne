package cafe.adriel.verne

import cafe.adriel.verne.domain.model.VerneConfig
import cafe.adriel.verne.logger.CrashlyticsLogger
import cafe.adriel.verne.logger.AndroidLogger
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val appModule = module {

    single {
        VerneConfig(
            releaseBuild = BuildConfig.RELEASE,
            appName = androidContext().getString(R.string.app_name),
            versionName = BuildConfig.VERSION_NAME,
            versionCode = BuildConfig.VERSION_CODE
        )
    }

    single {
        if (get<VerneConfig>().releaseBuild) CrashlyticsLogger
        else AndroidLogger
    }
}
