package cafe.adriel.verne.presentation.editor

import cafe.adriel.verne.model.TypographySettings
import com.etiennelenhart.eiffel.state.ViewState

data class EditorViewState(val editMode: Boolean = false, val settings: TypographySettings = TypographySettings()) : ViewState
