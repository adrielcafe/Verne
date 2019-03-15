package cafe.adriel.verne.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TypographySettings(
    val fontFamily: FontFamily = FontFamily.valueOf(DEFAULT_FONT_FAMILY),
    val fontSize: Int = DEFAULT_FONT_SIZE,
    val marginSize: Int = DEFAULT_MARGIN_SIZE
) : Parcelable {

    companion object {
        const val TYPOGRAPHY_FONT_FAMILY = "typographyFontFamily"
        const val TYPOGRAPHY_FONT_SIZE = "typographyFontSize"
        const val TYPOGRAPHY_MARGIN_SIZE = "typographyMarginSize"

        const val DEFAULT_FONT_FAMILY = "ALICE"
        const val DEFAULT_FONT_SIZE = 18
        const val DEFAULT_MARGIN_SIZE = 20
    }
}
