package cafe.adriel.verne.presentation.ui.editor.typography

import cafe.adriel.verne.presentation.model.FontFamily
import cafe.adriel.verne.presentation.model.TypographySettings
import cafe.adriel.verne.presentation.util.StateMachine

internal sealed class TypographyAction : StateMachine.Action {
    object Init : TypographyAction()
    data class SetFontFamily(val fontFamily: FontFamily) : TypographyAction()
    data class SetFontSize(val size: Int) : TypographyAction()
    data class SetMarginSize(val size: Int) : TypographyAction()
}

internal sealed class TypographyState : StateMachine.State {
    data class SettingsLoaded(val settings: TypographySettings) : TypographyState()
    object SettingsChanged : TypographyState()
}
