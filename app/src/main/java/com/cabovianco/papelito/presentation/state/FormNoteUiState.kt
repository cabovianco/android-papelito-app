package com.cabovianco.papelito.presentation.state

import com.cabovianco.papelito.domain.model.note.NoteColor
import com.cabovianco.papelito.domain.model.note.NoteFontFamily
import com.cabovianco.papelito.domain.model.note.NoteFontWeight

data class FormNoteUiState(
    val text: String = "",
    val backgroundColor: NoteColor = NoteColor.NEUTRAL_100,
    val fontColor: NoteColor = NoteColor.NEUTRAL_900,
    val fontSize: Float = 16F,
    val fontWeight: NoteFontWeight = NoteFontWeight.REGULAR,
    val fontFamily: NoteFontFamily = NoteFontFamily.FREDOKA
)
