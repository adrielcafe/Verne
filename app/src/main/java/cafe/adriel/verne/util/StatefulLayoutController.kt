package cafe.adriel.verne.util

import android.content.Context
import android.view.LayoutInflater
import cafe.adriel.verne.R
import cz.kinst.jakub.view.StatefulLayout

object StatefulLayoutController {

    const val STATE_CONTENT = "content"
    const val STATE_PROGRESS = "progress"
    const val STATE_NOT_FOUND = "notFound"
    const val STATE_EMPTY = "empty"

    fun createController(context: Context): StatefulLayout.StateController =
        StatefulLayout.StateController
            .create()
            .withState(STATE_PROGRESS, LayoutInflater.from(context).inflate(R.layout.state_loading, null))
            .withState(STATE_NOT_FOUND, LayoutInflater.from(context).inflate(R.layout.state_not_found, null))
            .withState(STATE_EMPTY, LayoutInflater.from(context).inflate(R.layout.state_empty, null))
            .build()

}