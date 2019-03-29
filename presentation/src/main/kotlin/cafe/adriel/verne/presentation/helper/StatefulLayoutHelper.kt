package cafe.adriel.verne.presentation.helper

import android.content.Context
import android.view.LayoutInflater
import cafe.adriel.verne.presentation.R
import cz.kinst.jakub.view.StatefulLayout

class StatefulLayoutHelper {

    companion object {
        const val STATE_CONTENT = "content"
        const val STATE_PROGRESS = "progress"
        const val STATE_NOT_FOUND = "notFound"
        const val STATE_EMPTY = "empty"
    }

    lateinit var controller: StatefulLayout.StateController

    fun init(context: Context) {
        controller = StatefulLayout.StateController
            .create()
            .withState(STATE_PROGRESS, LayoutInflater.from(context).inflate(R.layout.state_loading, null))
            .withState(STATE_NOT_FOUND, LayoutInflater.from(context).inflate(R.layout.state_not_found, null))
            .withState(STATE_EMPTY, LayoutInflater.from(context).inflate(R.layout.state_empty, null))
            .build()
    }
}
