package cafe.adriel.verne.di

import android.content.Context
import android.preference.PreferenceManager
import cafe.adriel.verne.BuildConfig
import cafe.adriel.verne.domain.model.BaseDir
import cafe.adriel.verne.shared.di.Component
import cafe.adriel.verne.shared.model.AppConfig
import org.koin.dsl.module

class AppComponent(private val appContext: Context) : Component {

    private val baseDirPath = "${appContext.filesDir}/${BuildConfig.BASE_DIR_NAME}"

    private val appModule = module {
        single {
            AppConfig(
                BuildConfig.VERSION_CODE,
                BuildConfig.VERSION_NAME,
                !BuildConfig.RELEASE
            )
        }
        single { BaseDir(baseDirPath) }
        single { PreferenceManager.getDefaultSharedPreferences(appContext) }
    }

    override fun getModules() = listOf(appModule)
}
