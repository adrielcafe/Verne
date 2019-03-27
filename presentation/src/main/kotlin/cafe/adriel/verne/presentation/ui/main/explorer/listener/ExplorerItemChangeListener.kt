package cafe.adriel.verne.presentation.ui.main.explorer.listener

import android.os.FileObserver
import java.io.File

class ExplorerItemChangeListener(dir: File, fireOnInit: Boolean, val listener: () -> Unit) :
    FileObserver(dir.path,
        EVENTS
    ) {

    companion object {
        private const val EVENTS =
            FileObserver.DELETE or
            FileObserver.CREATE or
            FileObserver.MOVED_TO or
            FileObserver.MOVED_FROM
    }

    init {
        if (fireOnInit) listener()
    }

    override fun onEvent(event: Int, path: String?) {
        if (EVENTS and event != 0) {
            listener()
        }
    }
}
