package cafe.adriel.verne.view.custom

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.core.widget.NestedScrollView
import cafe.adriel.verne.R
import cafe.adriel.verne.extension.color
import com.mixiaoxiao.fastscroll.FastScrollDelegate

class FastNestedScrollView(context: Context, attrs: AttributeSet) : NestedScrollView(context, attrs), FastScrollDelegate.FastScrollable {

    private val delegate: FastScrollDelegate by lazy {
        FastScrollDelegate.Builder(this)
            .thumbNormalColor(color(R.color.colorPrimary))
            .thumbPressedColor(color(R.color.colorPrimaryDark))
            .build()
    }

    override fun getFastScrollableView() = this

    override fun getFastScrollDelegate() = delegate

    override fun setNewFastScrollDelegate(newDelegate: FastScrollDelegate?) {
        // Do nothing
    }

    override fun onInterceptTouchEvent(ev: MotionEvent) =
        if (delegate.onInterceptTouchEvent(ev)) true else super.onInterceptTouchEvent(ev)

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent) =
        if (delegate.onTouchEvent(event)) true else super.onTouchEvent(event)

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        delegate.onAttachedToWindow()
    }

    override fun onWindowVisibilityChanged(visibility: Int) {
        super.onWindowVisibilityChanged(visibility)
        delegate.onWindowVisibilityChanged(visibility)
    }

    override fun onVisibilityChanged(changedView: View, visibility: Int) {
        super.onVisibilityChanged(changedView, visibility)
        delegate.onVisibilityChanged(changedView, visibility)
    }

    override fun dispatchDraw(canvas: Canvas) {
        super.dispatchDraw(canvas)
        delegate.dispatchDrawOver(canvas)
    }

    override fun awakenScrollBars() = delegate.awakenScrollBars()

    override fun superOnTouchEvent(event: MotionEvent) {
        super.onTouchEvent(event)
    }

    @SuppressLint("RestrictedApi")
    override fun superComputeVerticalScrollExtent() = super.computeVerticalScrollExtent()

    @SuppressLint("RestrictedApi")
    override fun superComputeVerticalScrollOffset() = super.computeVerticalScrollOffset()

    @SuppressLint("RestrictedApi")
    override fun superComputeVerticalScrollRange() = super.computeVerticalScrollRange()
}
