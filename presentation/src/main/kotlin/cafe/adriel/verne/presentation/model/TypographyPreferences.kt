package cafe.adriel.verne.presentation.model

import android.os.Parcelable
import cafe.adriel.verne.domain.repository.PreferencesRepository
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TypographyPreferences(
    val fontFamily: FontFamily = FontFamily.valueOf(PreferencesRepository.DEFAULT_FONT_FAMILY),
    val fontSize: Int = PreferencesRepository.DEFAULT_MARGIN_SIZE,
    val marginSize: Int = PreferencesRepository.DEFAULT_MARGIN_SIZE
) : Parcelable