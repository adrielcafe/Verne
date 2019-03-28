package cafe.adriel.verne.presentation.extension

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Parcelable
import android.view.WindowManager
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.preference.PreferenceManager
import cafe.adriel.verne.presentation.ui.main.settings.SettingsFragment
import cafe.adriel.verne.shared.extension.tagOf
import org.koin.ext.getFullName
import java.io.Serializable

fun Activity.isFullScreen() = window.attributes.flags and WindowManager.LayoutParams.FLAG_FULLSCREEN != 0

inline fun <reified T : Fragment> FragmentManager.getFragment() = findFragmentByTag(tagOf<T>()) as T?

inline fun <reified T : Activity> Context.intentFor(vararg extras: Pair<String, Any> = emptyArray()) =
    Intent(this, T::class.java).apply {
        extras.forEach {
            when (it.second) {
                is Parcelable -> putExtra(it.first, it.second as Parcelable)
                is Serializable -> putExtra(it.first, it.second as Serializable)
                is String -> putExtra(it.first, it.second as String)
                is Int -> putExtra(it.first, it.second as Int)
                else -> throw IllegalArgumentException("${it.second::class.getFullName()} not implemented")
            }
        }
    }

// TODO move to SettingsRepository
fun Context.isDarkMode() =
    PreferenceManager.getDefaultSharedPreferences(this).getBoolean(SettingsFragment.APP_DARK_MODE, false)

// TODO move to SettingsRepository
fun Context.isFullscreen() =
    PreferenceManager.getDefaultSharedPreferences(this).getBoolean(SettingsFragment.APP_FULLSCREEN, false)
