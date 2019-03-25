package cafe.adriel.verne.presentation.main.explorer

import cafe.adriel.verne.model.ExplorerItem
import com.etiennelenhart.eiffel.state.ViewState

data class ExplorerViewState(val items: List<ExplorerItem>? = null, val exception: Exception? = null) : ViewState
