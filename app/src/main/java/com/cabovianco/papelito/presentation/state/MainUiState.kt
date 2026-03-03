package com.cabovianco.papelito.presentation.state

import com.cabovianco.papelito.domain.model.AppError
import com.cabovianco.papelito.domain.model.note.Note

sealed interface MainUiState {
    data class Success(val notes: List<Note> = emptyList()) : MainUiState
    data class Error(val error: AppError) : MainUiState
    data object Loading : MainUiState
}
