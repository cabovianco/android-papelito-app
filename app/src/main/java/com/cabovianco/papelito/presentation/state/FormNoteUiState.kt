package com.cabovianco.papelito.presentation.state

import com.cabovianco.papelito.domain.model.NoteColor
import com.cabovianco.papelito.domain.model.NoteFontFamily
import com.cabovianco.papelito.domain.model.NoteFontWeight

data class FormNoteUiState(
    val noteId: Long = 0,
    val noteText: String = "",
    val noteBackgroundColor: NoteColor = NoteColor.WHITE,
    val noteFontColor: NoteColor = NoteColor.BLACK,
    val noteFontSize: Float = 16F,
    val noteFontWeight: NoteFontWeight = NoteFontWeight.NORMAL,
    val noteFontFamily: NoteFontFamily = NoteFontFamily.SANS_SERIF
)
