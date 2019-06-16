package cafe.adriel.verne.presentation.ui.main.explorer.listener

import cafe.adriel.verne.domain.model.ExplorerItem

internal interface ExplorerFragmentListener {

    fun onItemOpened(item: ExplorerItem)

    fun onPrintHtml(fileName: String, html: String)
}
