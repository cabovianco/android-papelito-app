package com.cabovianco.papelito.presentation.state

import com.cabovianco.papelito.domain.model.NoteColor

data class FormNoteUiState(
    val noteId: Long = 0,
    val noteText: String = "",
    val noteBackgroundColor: NoteColor = NoteColor.WHITE,
    val noteFontColor: NoteColor = NoteColor.BLACK
)
