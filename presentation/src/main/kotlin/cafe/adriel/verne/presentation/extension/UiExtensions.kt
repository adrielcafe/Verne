package cafe.adriel.verne.presentation.extension

import android.content.res.Resources
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.annotation.Px
import androidx.core.content.getSystemService
import androidx.transition.Fade
import androidx.transition.TransitionManager
import cafe.adriel.verne.presentation.R
import cz.kinst.jakub.view.StatefulLayout

fun EditText.showKeyboard() {
    context.getSystemService<InputMethodManager>()?.showSoftInput(this, 0)
}

fun EditText.hideKeyboard() {
    context.getSystemService<InputMethodManager>()?.hideSoftInputFromWindow(windowToken, 0)
}

fun View.showAnimated() {
    if (visibility != View.VISIBLE) {
        startAnimation(AnimationUtils.loadAnimation(context, R.anim.slide_in_bottom))
        visibility = View.VISIBLE
    }
}

fun View.hideAnimated() {
    if (visibility != View.GONE) {
        startAnimation(AnimationUtils.loadAnimation(context, R.anim.slide_out_bottom))
        visibility = View.GONE
    }
}

fun ViewGroup.setMargins(@Px start: Int = 0, @Px top: Int = 0, @Px right: Int = 0, @Px bottom: Int = 0) {
    when (val params = layoutParams) {
        is ViewGroup.MarginLayoutParams -> {
            params.setMargins(start, top, right, bottom)
            requestLayout()
        }
    }
}

fun StatefulLayout.StateController.setStateWithAnimation(layout: StatefulLayout, newState: String) {
    TransitionManager.beginDelayedTransition(layout, Fade())
    state = newState
}

fun Int.dpToPx() = (this * Resources.getSystem().displayMetrics.density).toInt()
fun Float.dpToPx() = this * Resources.getSystem().displayMetrics.density

fun Int.pxToDp() = (this / Resources.getSystem().displayMetrics.density).toInt()
fun Float.pxToDp() = this / Resources.getSystem().displayMetrics.density
