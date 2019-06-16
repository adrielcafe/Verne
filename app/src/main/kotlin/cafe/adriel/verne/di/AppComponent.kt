package cafe.adriel.verne.di

import android.content.Context
import android.preference.PreferenceManager
import cafe.adriel.pufferdb.core.PufferDB
import cafe.adriel.verne.BuildConfig
import cafe.adriel.verne.shared.di.Component
import cafe.adriel.verne.shared.model.AppConfig
import org.koin.dsl.module
import java.io.File

class AppComponent(appContext: Context) : Component {

    private val settingsFile = File("${appContext.filesDir}/${BuildConfig.SETTINGS_FILE_NAME}")
    private val explorerRootFolder = File("${appContext.filesDir}/${BuildConfig.EXPLORER_ROOT_FOLDER_NAME}")

    private val appModule = module {
        single {
            AppConfig(
                versionCode = BuildConfig.VERSION_CODE,
                versionName = BuildConfig.VERSION_NAME,
                isDebug = !BuildConfig.RELEASE,
                explorerRootFolder = explorerRootFolder
            )
        }
        single { PreferenceManager.getDefaultSharedPreferences(appContext) }
        single { PufferDB.with(settingsFile) }
    }

    override fun getModules() = listOf(appModule)
}
