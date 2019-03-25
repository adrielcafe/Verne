package cafe.adriel.verne.presentation.main.explorer.listener

import cafe.adriel.verne.model.ExplorerItem

interface ExplorerFragmentListener {

    fun onPrintHtml(fileName: String, html: String)

    fun onItemOpened(item: ExplorerItem)
}
