package cafe.adriel.verne.presentation.extension

import android.content.Context
import android.graphics.Color
import androidx.annotation.AttrRes
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.FontRes
import androidx.annotation.IntegerRes
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.fragment.app.Fragment
import cafe.adriel.verne.shared.extension.withIO

fun Context.color(@ColorRes resId: Int) = ResourcesCompat.getColor(resources, resId, theme)
fun Fragment.color(@ColorRes resId: Int) = context?.color(resId) ?: Color.TRANSPARENT

fun Context.colorFromAttr(@AttrRes resId: Int): Int {
    val attrs = obtainStyledAttributes(intArrayOf(resId))
    val color = attrs.getColor(0, 0)
    attrs.recycle()
    return color
}
fun Fragment.colorFromAttr(@AttrRes resId: Int) = context?.colorFromAttr(resId) ?: Color.TRANSPARENT

fun Context.int(@IntegerRes resId: Int) = resources.getInteger(resId)
fun Fragment.int(@IntegerRes resId: Int) = context?.int(resId) ?: -1

fun Context.long(@IntegerRes resId: Int) = resources.getInteger(resId).toLong()
fun Fragment.long(@IntegerRes resId: Int) = context?.long(resId) ?: -1

fun Context.drawable(@DrawableRes resId: Int, tintColor: Int? = null) =
    ResourcesCompat.getDrawable(resources, resId, theme)?.apply {
        if (tintColor != null) {
            DrawableCompat.setTint(this, tintColor)
        }
    }

suspend fun Context.font(@FontRes resId: Int) = withIO {
    ResourcesCompat.getFont(this, resId)
}
