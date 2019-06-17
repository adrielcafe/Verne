package cafe.adriel.verne.presentation.ui.editor.typography

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cafe.adriel.verne.domain.interactor.settings.FontFamilySettingsInteractor
import cafe.adriel.verne.domain.interactor.settings.FontSizeSettingsInteractor
import cafe.adriel.verne.domain.interactor.settings.MarginSizeSettingsInteractor
import cafe.adriel.verne.presentation.model.FontFamily
import cafe.adriel.verne.presentation.model.TypographySettings
import cafe.adriel.verne.presentation.util.StateMachineViewModel
import cafe.adriel.verne.shared.extension.withDefault
import kotlinx.coroutines.launch

internal class TypographyViewModel(
    private val fontFamilyInteractor: FontFamilySettingsInteractor,
    private val fontSizeInteractor: FontSizeSettingsInteractor,
    private val marginSizeInteractor: MarginSizeSettingsInteractor
) : ViewModel(), StateMachineViewModel<TypographyAction, TypographyState> {

    override val state = MutableLiveData<TypographyState>()

    init {
        setAction(TypographyAction.Init)
    }

    override fun setAction(action: TypographyAction) {
        viewModelScope.launch {
            state.value = when (action) {
                is TypographyAction.Init -> TypographyState.Init(loadSettings())

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
        }
    }

    private suspend fun loadSettings() = withDefault {
        TypographySettings(
            FontFamily.valueOf(fontFamilyInteractor.get()),
            fontSizeInteractor.get(),
            marginSizeInteractor.get()
        )
    }
}
