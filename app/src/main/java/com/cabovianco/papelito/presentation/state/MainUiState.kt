package com.cabovianco.papelito.presentation.state

import com.cabovianco.papelito.domain.model.Note

sealed interface MainUiState {
    data class Success(val notes: List<Note> = emptyList()): MainUiState
    data object Loading: MainUiState
}
