package cafe.adriel.verne.presentation.ui.main

import android.content.Context
import cafe.adriel.androidcoroutinescopes.viewmodel.CoroutineScopedViewModel
import cafe.adriel.verne.shared.model.AppConfig
import com.uttampanchasara.pdfgenerator.CreatePdf

class MainViewModel(appConfig: AppConfig) : CoroutineScopedViewModel() {

    val appVersion = "v${appConfig.versionName} (Build ${appConfig.versionCode})"

    fun printHtml(context: Context, fileName: String, html: String) {
        CreatePdf(context)
            .setPdfName(fileName)
            .setContent(html)
            .openPrintDialog(true)
            .create()
    }
}
