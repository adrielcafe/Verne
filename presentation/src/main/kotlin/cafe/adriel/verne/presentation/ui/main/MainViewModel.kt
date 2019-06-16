package cafe.adriel.verne.presentation.ui.main

import androidx.lifecycle.ViewModel
import cafe.adriel.verne.shared.model.AppConfig

internal class MainViewModel(appConfig: AppConfig) : ViewModel() {

    val appVersion = "v${appConfig.versionName} (Build ${appConfig.versionCode})"
}
