package cafe.adriel.verne.presentation.helper

import android.content.Context
import androidx.core.os.bundleOf
import cafe.adriel.verne.shared.model.AppConfig
import com.google.firebase.analytics.FirebaseAnalytics

internal class AnalyticsHelper(appContext: Context, appConfig: AppConfig) {

    companion object {
        private const val EVENT_OPEN_FILE = "open_file"
        private const val EVENT_NEW_FILE = "new_file"
        private const val EVENT_NEW_FOLDER = "new_folder"
        private const val EVENT_TEXT_SEARCH = "text_search"
        private const val EVENT_VOICE_SEARCH = "voice_search"
        private const val EVENT_SHARE = "share"
        private const val EVENT_PRINT = "print"
        private const val EVENT_SWITCH_DARK_MODE = "switch_dark_mode"
        private const val EVENT_SWITCH_FULL_SCREEN = "switch_full_screen"
        private const val EVENT_SHOW_STATISTICS = "show_statistics"
        private const val EVENT_TYPOGRAPHY_FONT_FAMILY = "typo_font_family"
        private const val EVENT_TYPOGRAPHY_FONT_SIZE = "typo_font_size"
        private const val EVENT_TYPOGRAPHY_MARGIN_SIZE = "typo_margin_size"

        const val NEW_FILE_SOURCE_INTERNAL = "internal"
        const val NEW_FILE_SOURCE_EXTERNAL = "external"
        const val NEW_FILE_SOURCE_SHORTCUT = "shortcut"
    }

    private var analytics: FirebaseAnalytics? = null

    init {
        if (!appConfig.isDebug) analytics = FirebaseAnalytics.getInstance(appContext)
    }

    fun logOpenFile() {
        analytics?.logEvent(EVENT_OPEN_FILE, null)
    }

    fun logNewFile(source: String) {
        analytics?.logEvent(
            EVENT_NEW_FILE,
            bundleOf(FirebaseAnalytics.Param.SOURCE to source)
        )
    }

    fun logNewFolder() {
        analytics?.logEvent(EVENT_NEW_FOLDER, null)
    }

    fun logTextSearch() {
        analytics?.logEvent(EVENT_TEXT_SEARCH, null)
    }

    fun logVoiceSearch() {
        analytics?.logEvent(EVENT_VOICE_SEARCH, null)
    }

    fun logShare() {
        analytics?.logEvent(EVENT_SHARE, null)
    }

    fun logPrint() {
        analytics?.logEvent(EVENT_PRINT, null)
    }

    fun logSwitchDarkMode(value: Boolean) {
        analytics?.logEvent(
            EVENT_SWITCH_DARK_MODE,
            bundleOf(FirebaseAnalytics.Param.VALUE to value)
        )
    }

    fun logSwitchFullScreen(value: Boolean) {
        analytics?.logEvent(
            EVENT_SWITCH_FULL_SCREEN,
            bundleOf(FirebaseAnalytics.Param.VALUE to value)
        )
    }

    fun logShowStatistics() {
        analytics?.logEvent(EVENT_SHOW_STATISTICS, null)
    }

    fun logTypographyFontFamily(value: String) {
        analytics?.logEvent(
            EVENT_TYPOGRAPHY_FONT_FAMILY,
            bundleOf(FirebaseAnalytics.Param.VALUE to value)
        )
    }

    fun logTypographyFontSize(value: Int) {
        analytics?.logEvent(
            EVENT_TYPOGRAPHY_FONT_SIZE,
            bundleOf(FirebaseAnalytics.Param.VALUE to value)
        )
    }

    fun logTypographyMarginSize(value: Int) {
        analytics?.logEvent(
            EVENT_TYPOGRAPHY_MARGIN_SIZE,
            bundleOf(FirebaseAnalytics.Param.VALUE to value)
        )
    }
}
