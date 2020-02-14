package cafe.adriel.verne

import cafe.adriel.verne.domain.model.VerneConfig
import java.io.File
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val appModule = module {

    single {
        VerneConfig(
            releaseBuild = BuildConfig.RELEASE,
            versionCode = BuildConfig.VERSION_CODE,
            versionName = BuildConfig.VERSION_NAME,
            databaseDir = File(androidContext().filesDir, VerneConfig.DATABASE_NAME)
        )
    }
}
