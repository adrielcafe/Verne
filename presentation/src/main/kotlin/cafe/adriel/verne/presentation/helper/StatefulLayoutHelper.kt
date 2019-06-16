package cafe.adriel.verne.presentation.helper

import android.content.Context
import android.view.LayoutInflater
import cafe.adriel.verne.presentation.R
import cz.kinst.jakub.view.StatefulLayout

internal class StatefulLayoutHelper {

    companion object {
        const val STATE_CONTENT = "content"
        const val STATE_PROGRESS = "progress"
        const val STATE_NOT_FOUND = "notFound"
        const val STATE_EMPTY = "empty"
    }

    lateinit var controller: StatefulLayout.StateController

    fun init(context: Context) {
        LayoutInflater.from(context).apply {
            controller = StatefulLayout.StateController
                .create()
                .withState(STATE_PROGRESS, inflate(R.layout.state_loading, null))
                .withState(STATE_NOT_FOUND, inflate(R.layout.state_not_found, null))
                .withState(STATE_EMPTY, inflate(R.layout.state_empty, null))
                .build()
        }
    }
}
