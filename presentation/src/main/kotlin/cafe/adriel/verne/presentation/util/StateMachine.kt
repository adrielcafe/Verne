package cafe.adriel.verne.presentation.util

import androidx.lifecycle.LiveData

internal interface StateMachineAction

internal interface StateMachineState

internal interface StateMachineView<S : StateMachineState> {

    fun onState(state: S)
}

internal interface StateMachineViewModel<A : StateMachineAction, S : StateMachineState> {

    val state: LiveData<S>

    fun setAction(action: A)
}
