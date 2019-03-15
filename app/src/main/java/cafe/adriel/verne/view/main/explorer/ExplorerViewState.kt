package cafe.adriel.verne.view.main.explorer

import cafe.adriel.verne.model.ExplorerItem
import com.etiennelenhart.eiffel.state.ViewState

data class ExplorerViewState(val items: List<ExplorerItem>? = null) : ViewState