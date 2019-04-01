package cafe.adriel.verne.presentation.model

import android.os.Parcelable
import cafe.adriel.verne.domain.repository.SettingsRepository.Companion.DEFAULT_FONT_FAMILY
import cafe.adriel.verne.domain.repository.SettingsRepository.Companion.DEFAULT_FONT_SIZE
import cafe.adriel.verne.domain.repository.SettingsRepository.Companion.DEFAULT_MARGIN_SIZE
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TypographySettings(
    val fontFamily: FontFamily = FontFamily.valueOf(DEFAULT_FONT_FAMILY),
    val fontSize: Int = DEFAULT_FONT_SIZE,
    val marginSize: Int = DEFAULT_MARGIN_SIZE
) : Parcelable
