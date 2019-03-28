package cafe.adriel.verne.presentation.model

import android.os.Parcelable
import cafe.adriel.verne.domain.repository.SettingsRepository
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TypographySettings(
    val fontFamily: FontFamily = FontFamily.valueOf(SettingsRepository.DEFAULT_FONT_FAMILY),
    val fontSize: Int = SettingsRepository.DEFAULT_MARGIN_SIZE,
    val marginSize: Int = SettingsRepository.DEFAULT_MARGIN_SIZE
) : Parcelable