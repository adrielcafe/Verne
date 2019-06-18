package cafe.adriel.verne.presentation.util

import androidx.lifecycle.LiveData

internal interface StateMachine {

    interface Action

    interface State

    interface View<S : State> {

        fun onState(state: S)
    }

    interface ViewModel<A : Action, S : State> {

        val state: LiveData<S>

        operator fun plus(action: A)
    }
}
