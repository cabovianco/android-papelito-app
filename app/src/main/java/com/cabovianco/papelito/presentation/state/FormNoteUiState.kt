package com.cabovianco.papelito.presentation.state

import com.cabovianco.papelito.domain.model.note.NoteColor
import com.cabovianco.papelito.domain.model.note.NoteFontFamily
import com.cabovianco.papelito.domain.model.note.NoteFontWeight

data class FormNoteUiState(
    val text: String = "",
    val backgroundColor: NoteColor = NoteColor.WHITE,
    val fontColor: NoteColor = NoteColor.BLACK,
    val fontSize: Float = 16F,
    val fontWeight: NoteFontWeight = NoteFontWeight.NORMAL,
    val fontFamily: NoteFontFamily = NoteFontFamily.SANS_SERIF
)
