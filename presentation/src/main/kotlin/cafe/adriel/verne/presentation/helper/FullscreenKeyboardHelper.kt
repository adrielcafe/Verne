package cafe.adriel.verne.presentation.helper

import android.app.Activity
import android.graphics.Rect
import android.view.ViewGroup
import android.widget.FrameLayout
import cafe.adriel.verne.presentation.extension.isFullscreen

// Based on https://stackoverflow.com/a/42261118/1055354
class FullscreenKeyboardHelper {
    private lateinit var contentContainer: ViewGroup

    private val rootView by lazy { contentContainer.getChildAt(0) }
    private val rootViewLayout by lazy { rootView.layoutParams as FrameLayout.LayoutParams }
    private val viewTreeObserver by lazy { rootView.viewTreeObserver }

    private val listener = { possiblyResizeChildOfContent() }
    private val contentAreaOfWindowBounds = Rect()
    private var usableHeightPrevious = 0
    private var isFullscreen = false

    fun init(activity: Activity){
        contentContainer = activity.findViewById(android.R.id.content)
        isFullscreen = activity.isFullscreen()
    }

    fun addListener() {
        if (viewTreeObserver.isAlive)
            viewTreeObserver.addOnGlobalLayoutListener(listener)
    }

    fun removeListener() {
        if (viewTreeObserver.isAlive)
            viewTreeObserver.removeOnGlobalLayoutListener(listener)
    }

    private fun possiblyResizeChildOfContent() {
        contentContainer.getWindowVisibleDisplayFrame(contentAreaOfWindowBounds)
        val usableHeightNow = contentAreaOfWindowBounds.height()
        if (isFullscreen && usableHeightNow != usableHeightPrevious) {
            rootViewLayout.height = usableHeightNow
            rootView.layout(contentAreaOfWindowBounds.left, contentAreaOfWindowBounds.top, contentAreaOfWindowBounds.right, contentAreaOfWindowBounds.bottom)
            rootView.requestLayout()
            usableHeightPrevious = usableHeightNow
        }
    }
}
