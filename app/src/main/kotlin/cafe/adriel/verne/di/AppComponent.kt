package cafe.adriel.verne.di

import android.content.Context
import android.preference.PreferenceManager
import cafe.adriel.verne.App
import cafe.adriel.verne.domain.model.BaseDir
import cafe.adriel.verne.shared.di.Component
import org.koin.dsl.module

class AppComponent(private val appContext: Context) : Component {

    private val baseDirPath = "${appContext.filesDir}/${App.BASE_DIR_NAME}"

    private val appModule = module {
        single { PreferenceManager.getDefaultSharedPreferences(appContext) }
        single { BaseDir(baseDirPath) }
    }

    override fun getModules() = listOf(appModule)
}
