package cafe.adriel.verne.presentation.ui.main.explorer

import cafe.adriel.verne.domain.model.ExplorerItem
import com.etiennelenhart.eiffel.state.ViewState

data class ExplorerViewState(
    val items: List<ExplorerItem>? = null,
    val exception: Exception? = null
) : ViewState
