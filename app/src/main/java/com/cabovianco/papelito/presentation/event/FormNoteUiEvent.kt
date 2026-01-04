package com.cabovianco.papelito.presentation.event

sealed class FormNoteUiEvent {
    data object Saved: FormNoteUiEvent()
    data class Error(val msg: String?): FormNoteUiEvent()
}
