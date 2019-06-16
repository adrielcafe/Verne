package cafe.adriel.verne.presentation.util

import com.etiennelenhart.eiffel.state.ViewState
import com.etiennelenhart.eiffel.viewmodel.StateViewModel

internal interface StateAware<S : ViewState> {

    val viewModel: StateViewModel<S>

    fun onStateUpdated(state: S)
}
