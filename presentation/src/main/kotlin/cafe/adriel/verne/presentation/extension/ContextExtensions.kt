package cafe.adriel.verne.presentation.extension

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import cafe.adriel.verne.shared.extension.tagOf
import com.google.android.material.snackbar.Snackbar

inline fun <reified T : Fragment> FragmentManager.getFragment() = findFragmentByTag(tagOf<T>()) as T?

inline fun <reified T : Activity> Context.intent() = Intent(this, T::class.java)

fun Activity.showSnackBar(message: String) =
    Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_SHORT).show()
fun Activity.showSnackBar(@StringRes resId: Int) = showSnackBar(getString(resId))
