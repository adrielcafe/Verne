package cafe.adriel.verne.presentation.ui.main.explorer.listener

import android.os.FileObserver
import java.io.File

internal class ExplorerItemChangeListener(
    dir: File,
    fireOnInit: Boolean,
    val listener: () -> Unit
) : FileObserver(dir.path, EVENTS) {

    companion object {
        private const val EVENTS = DELETE or CREATE or MOVED_TO or MOVED_FROM
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
