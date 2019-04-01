package cafe.adriel.verne.di

import android.content.Context
import android.preference.PreferenceManager
import cafe.adriel.verne.BuildConfig
import cafe.adriel.verne.shared.di.Component
import cafe.adriel.verne.shared.model.AppConfig
import org.koin.dsl.module
import java.io.File

class AppComponent(appContext: Context) : Component {

    private val explorerRootFolder = File("${appContext.filesDir}/${BuildConfig.EXPLORER_ROOT_FOLDER_NAME}")
    private val settingsFile = File("${appContext.filesDir}/${BuildConfig.SETTINGS_FILE_NAME}")
    private val appConfig = AppConfig(
        BuildConfig.VERSION_CODE,
        BuildConfig.VERSION_NAME,
        !BuildConfig.RELEASE,
        explorerRootFolder,
        settingsFile
    )

    private val appModule = module {
        single { appConfig }
        single { PreferenceManager.getDefaultSharedPreferences(appContext) }
    }

    override fun getModules() = listOf(appModule)
}
