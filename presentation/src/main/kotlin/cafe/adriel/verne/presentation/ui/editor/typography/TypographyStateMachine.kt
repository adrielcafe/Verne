package cafe.adriel.verne.presentation.ui.editor.typography

import cafe.adriel.verne.presentation.model.FontFamily
import cafe.adriel.verne.presentation.model.TypographySettings
import cafe.adriel.verne.presentation.util.StateMachineAction
import cafe.adriel.verne.presentation.util.StateMachineState

internal sealed class TypographyAction : StateMachineAction {
    object Init : TypographyAction()
    data class SetFontFamily(val fontFamily: FontFamily) : TypographyAction()
    data class SetFontSize(val size: Int) : TypographyAction()
    data class SetMarginSize(val size: Int) : TypographyAction()
}

internal sealed class TypographyState : StateMachineState {
    data class Init(val settings: TypographySettings) : TypographyState()
    object SettingsChanged : TypographyState()
}
