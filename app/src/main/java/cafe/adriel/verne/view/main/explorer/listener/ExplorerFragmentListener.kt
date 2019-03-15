package cafe.adriel.verne.view.main.explorer.listener

import cafe.adriel.verne.model.ExplorerItem

interface ExplorerFragmentListener {

    fun onPrintHtml(fileName: String, html: String)

    fun onItemOpened(item: ExplorerItem)
}
