package cafe.adriel.verne.presentation.extension

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.view.View
import androidx.annotation.AttrRes
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.FontRes
import androidx.annotation.IntegerRes
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.fragment.app.Fragment
import com.github.ajalt.timberkt.e

fun Context.color(@ColorRes resId: Int) = ResourcesCompat.getColor(resources, resId, theme)
fun Fragment.color(@ColorRes resId: Int) = context?.color(resId) ?: Color.TRANSPARENT
fun View.color(@ColorRes resId: Int) = context.color(resId)

fun Context.colorFromAttr(@AttrRes resId: Int): Int {
    val attrs = obtainStyledAttributes(intArrayOf(resId))
    val color = attrs.getColor(0, 0)
    attrs.recycle()
    return color
}
fun Fragment.colorFromAttr(@AttrRes resId: Int) = context?.colorFromAttr(resId) ?: Color.TRANSPARENT
fun View.colorFromAttr(@AttrRes resId: Int) = context.colorFromAttr(resId)

fun Context.int(@IntegerRes resId: Int) = resources.getInteger(resId)
fun Fragment.int(@IntegerRes resId: Int) = context?.int(resId) ?: -1
fun View.int(@IntegerRes resId: Int) = context.int(resId)

fun Context.long(@IntegerRes resId: Int) = resources.getInteger(resId).toLong()
fun Fragment.long(@IntegerRes resId: Int) = context?.long(resId) ?: -1
fun View.long(@IntegerRes resId: Int) = context.long(resId)

fun Context.font(@FontRes resId: Int, callback: (typeface: Typeface) -> Unit) =
    ResourcesCompat.getFont(this, resId, object : ResourcesCompat.FontCallback() {
        override fun onFontRetrieved(typeface: Typeface) {
            callback(typeface)
        }
        override fun onFontRetrievalFailed(reason: Int) {
            e { "Font retrieval failed: $reason" }
        }
    }, null)
fun Fragment.font(@FontRes resId: Int, callback: (typeface: Typeface) -> Unit) = context?.font(resId, callback)
fun View.font(@FontRes resId: Int, callback: (typeface: Typeface) -> Unit) = context.font(resId, callback)

fun Context.drawable(@DrawableRes resId: Int, tintColor: Int? = null) =
    ResourcesCompat.getDrawable(resources, resId, theme)?.apply {
        if (tintColor != null) {
            DrawableCompat.setTint(this, tintColor)
        }
    }
fun Fragment.drawable(@DrawableRes resId: Int, tintColor: Int? = null) = context?.drawable(resId, tintColor)
fun View.drawable(@DrawableRes resId: Int, tintColor: Int? = null) = context?.drawable(resId, tintColor)
