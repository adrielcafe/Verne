package cafe.adriel.verne.presentation.ui.main

import android.content.Context
import androidx.lifecycle.MutableLiveData
import cafe.adriel.verne.shared.model.AppConfig
import cafe.adriel.verne.presentation.util.CoroutineScopedStateViewModel
import com.uttampanchasara.pdfgenerator.CreatePdf

class MainViewModel(appConfig: AppConfig) : CoroutineScopedStateViewModel<MainViewState>() {

    val appVersion = "v${appConfig.versionName} (Build ${appConfig.versionCode})"

    override val state = MutableLiveData<MainViewState>()

    init {
        initState { MainViewState() }
    }

    fun printHtml(context: Context, fileName: String, html: String) {
        CreatePdf(context)
            .setPdfName(fileName)
            .setContent(html)
            .openPrintDialog(true)
            .create()
    }
}
