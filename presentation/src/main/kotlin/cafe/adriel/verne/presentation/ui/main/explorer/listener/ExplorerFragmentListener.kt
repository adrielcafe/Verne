package cafe.adriel.verne.presentation.ui.main.explorer.listener

import cafe.adriel.verne.domain.model.ExplorerItem

interface ExplorerFragmentListener {

    fun onPrintHtml(fileName: String, html: String)

    fun onItemOpened(item: ExplorerItem)
}
