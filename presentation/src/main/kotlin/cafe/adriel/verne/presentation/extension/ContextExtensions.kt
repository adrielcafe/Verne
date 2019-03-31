package cafe.adriel.verne.presentation.extension

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Parcelable
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import cafe.adriel.verne.shared.extension.tagOf
import com.google.android.material.snackbar.Snackbar
import org.koin.ext.getFullName
import java.io.Serializable

inline fun <reified T : Fragment> FragmentManager.getFragment() = findFragmentByTag(tagOf<T>()) as T?

inline fun <reified T : Activity> Context.intentFor(vararg extras: Pair<String, Any> = emptyArray()) =
    Intent(this, T::class.java).apply {
        extras.forEach { param ->
            when (param.second) {
                is Parcelable -> putExtra(param.first, param.second as Parcelable)
                is Serializable -> putExtra(param.first, param.second as Serializable)
                is String -> putExtra(param.first, param.second as String)
                is Int -> putExtra(param.first, param.second as Int)
                else -> throw IllegalArgumentException("${param.second::class.getFullName()} not implemented")
            }
        }
    }

fun Activity.showSnackBar(message: String) =
    Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_SHORT).show()
fun Activity.showSnackBar(@StringRes resId: Int) = showSnackBar(getString(resId))
