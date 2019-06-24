package cafe.adriel.verne.presentation.ui.editor.typography

import cafe.adriel.hal.HAL
import cafe.adriel.verne.presentation.model.FontFamily
import cafe.adriel.verne.presentation.model.TypographySettings

internal sealed class TypographyAction : HAL.Action {
    object LoadSettings : TypographyAction()
    data class SetFontFamily(val fontFamily: FontFamily) : TypographyAction()
    data class SetFontSize(val size: Int) : TypographyAction()
    data class SetMarginSize(val size: Int) : TypographyAction()
}

internal sealed class TypographyState : HAL.State {
    object Init : TypographyState()
    data class SettingsLoaded(val settings: TypographySettings) : TypographyState()
    object SettingsChanged : TypographyState()
}
