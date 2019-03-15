package cafe.adriel.verne.util

import com.etiennelenhart.eiffel.state.ViewState
import com.etiennelenhart.eiffel.viewmodel.StateViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext

abstract class CoroutineScopedStateViewModel<T : ViewState> : StateViewModel<T>(), CoroutineScope {

    private val coroutineJob = Job()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + coroutineJob

    override fun onCleared() {
        super.onCleared()
        coroutineJob.cancel()
    }
}
