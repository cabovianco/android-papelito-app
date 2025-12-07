package com.cabovianco.papelito.presentation.state

import com.cabovianco.papelito.domain.model.Color

data class FormNoteUiState(
    val noteId: Long = 0,
    val noteText: String = "",
    val noteBackgroundColor: Color = Color.WHITE,
    val noteFontColor: Color = Color.BLACK
)
