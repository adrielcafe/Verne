package cafe.adriel.verne.presentation.helper

import android.content.Context
import com.uttampanchasara.pdfgenerator.CreatePdf

internal class HtmlPrinterHelper {

    fun printHtml(context: Context, fileName: String, html: String) {
        CreatePdf(context)
            .setPdfName(fileName)
            .setContent(html)
            .openPrintDialog(true)
            .create()
    }
}
