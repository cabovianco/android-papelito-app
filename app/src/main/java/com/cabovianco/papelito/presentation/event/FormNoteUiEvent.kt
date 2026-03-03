package com.cabovianco.papelito.presentation.event

import com.cabovianco.papelito.domain.model.AppError

sealed class FormNoteUiEvent {
    data object Saved : FormNoteUiEvent()
    data class Error(val error: AppError) : FormNoteUiEvent()
}
