package cafe.adriel.verne.presentation.ui.editor

import cafe.adriel.verne.presentation.model.TypographyPreferences
import com.etiennelenhart.eiffel.state.ViewState

data class EditorViewState(
    val editMode: Boolean = false,
    val preferences: TypographyPreferences = TypographyPreferences()
) : ViewState
