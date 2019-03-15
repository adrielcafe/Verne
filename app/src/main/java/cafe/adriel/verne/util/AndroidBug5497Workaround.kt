package cafe.adriel.verne.util

import android.app.Activity
import android.graphics.Rect
import android.view.ViewGroup
import android.widget.FrameLayout
import cafe.adriel.verne.extension.isFullScreen

// Based on https://stackoverflow.com/a/42261118/1055354
class AndroidBug5497Workaround(private val activity: Activity) {
    private val contentContainer = activity.findViewById<ViewGroup>(android.R.id.content)
    private val rootView = contentContainer.getChildAt(0)
    private val rootViewLayout = rootView.layoutParams as FrameLayout.LayoutParams
    private val viewTreeObserver = rootView.viewTreeObserver
    private val listener = { possiblyResizeChildOfContent() }
    private val contentAreaOfWindowBounds = Rect()
    private var usableHeightPrevious = 0

    fun addListener() {
        if(viewTreeObserver.isAlive)
            viewTreeObserver.addOnGlobalLayoutListener(listener)
    }

    fun removeListener() {
        if(viewTreeObserver.isAlive)
            viewTreeObserver.removeOnGlobalLayoutListener(listener)
    }

    private fun possiblyResizeChildOfContent() {
        contentContainer.getWindowVisibleDisplayFrame(contentAreaOfWindowBounds)
        val usableHeightNow = contentAreaOfWindowBounds.height()
        if (activity.isFullScreen() && usableHeightNow != usableHeightPrevious) {
            rootViewLayout.height = usableHeightNow
            rootView.layout(contentAreaOfWindowBounds.left, contentAreaOfWindowBounds.top, contentAreaOfWindowBounds.right, contentAreaOfWindowBounds.bottom)
            rootView.requestLayout()
            usableHeightPrevious = usableHeightNow
        }
    }
}