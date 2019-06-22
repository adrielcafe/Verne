package cafe.adriel.verne.presentation.ui.editor.typography

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cafe.adriel.hal.HAL
import cafe.adriel.hal.plus
import cafe.adriel.verne.domain.interactor.settings.FontFamilySettingsInteractor
import cafe.adriel.verne.domain.interactor.settings.FontSizeSettingsInteractor
import cafe.adriel.verne.domain.interactor.settings.MarginSizeSettingsInteractor
import cafe.adriel.verne.presentation.model.FontFamily
import cafe.adriel.verne.presentation.model.TypographySettings

internal class TypographyViewModel(
    private val fontFamilyInteractor: FontFamilySettingsInteractor,
    private val fontSizeInteractor: FontSizeSettingsInteractor,
    private val marginSizeInteractor: MarginSizeSettingsInteractor
) : ViewModel(), HAL.StateMachine<TypographyAction, TypographyState> {

    override val hal by HAL(viewModelScope, ::reducer)

    init {
        this + TypographyAction.Init
    }

    private suspend fun reducer(action: TypographyAction): TypographyState = when (action) {
        is TypographyAction.Init -> TypographyState.SettingsLoaded(loadSettings())

        is TypographyAction.SetFontFamily -> {
            fontFamilyInteractor.set(action.fontFamily.name)
            TypographyState.SettingsChanged
        }

        is TypographyAction.SetFontSize -> {
            fontSizeInteractor.set(action.size)
            TypographyState.SettingsChanged
        }

        is TypographyAction.SetMarginSize -> {
            marginSizeInteractor.set(action.size)
            TypographyState.SettingsChanged
        }
    }

    private suspend fun loadSettings() =
        TypographySettings(
            FontFamily.valueOf(fontFamilyInteractor.get()),
            fontSizeInteractor.get(),
            marginSizeInteractor.get()
        )
}
