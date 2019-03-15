package cafe.adriel.verne.view.main

import android.content.Context
import androidx.lifecycle.MutableLiveData
import cafe.adriel.verne.BuildConfig
import cafe.adriel.verne.util.CoroutineScopedStateViewModel
import com.uttampanchasara.pdfgenerator.CreatePdf

class MainViewModel : CoroutineScopedStateViewModel<MainViewState>() {

    val appVersion = "v${BuildConfig.VERSION_NAME} (Build ${BuildConfig.VERSION_CODE})"

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
