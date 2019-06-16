package cafe.adriel.verne.presentation.helper

import android.graphics.Rect
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent

// Based on https://stackoverflow.com/a/42261118/1055354
internal class FullscreenKeyboardHelper(private val preferencesHelper: PreferencesHelper) : LifecycleObserver {
    private lateinit var contentContainer: ViewGroup

    private val rootView by lazy { contentContainer.getChildAt(0) }
    private val rootViewLayout by lazy { rootView.layoutParams as FrameLayout.LayoutParams }
    private val viewTreeObserver by lazy { rootView.viewTreeObserver }

    private val listener = { possiblyResizeChildOfContent() }
    private val contentAreaOfWindowBounds = Rect()
    private var usableHeightPrevious = 0

    fun init(activity: AppCompatActivity) {
        contentContainer = activity.findViewById(android.R.id.content)
        activity.lifecycle.addObserver(this)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    private fun addListener() {
        if (viewTreeObserver.isAlive) {
            viewTreeObserver.addOnGlobalLayoutListener(listener)
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    private fun removeListener() {
        if (viewTreeObserver.isAlive) {
            viewTreeObserver.removeOnGlobalLayoutListener(listener)
        }
    }

    private fun possiblyResizeChildOfContent() {
        contentContainer.getWindowVisibleDisplayFrame(contentAreaOfWindowBounds)
        val usableHeightNow = contentAreaOfWindowBounds.height()
        if (preferencesHelper.isFullscreen() && usableHeightNow != usableHeightPrevious) {
            rootViewLayout.height = usableHeightNow
            rootView.layout(
                contentAreaOfWindowBounds.left,
                contentAreaOfWindowBounds.top,
                contentAreaOfWindowBounds.right,
                contentAreaOfWindowBounds.bottom
            )
            rootView.requestLayout()
            usableHeightPrevious = usableHeightNow
        }
    }
}
