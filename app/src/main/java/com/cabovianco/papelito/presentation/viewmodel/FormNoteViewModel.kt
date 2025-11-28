package com.cabovianco.papelito.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.cabovianco.papelito.domain.model.Note
import com.cabovianco.papelito.presentation.state.NoteFormUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

open class FormNoteViewModel : ViewModel() {
    protected val _uiState: MutableStateFlow<NoteFormUiState> =
        MutableStateFlow(NoteFormUiState())

    val uiState get() = _uiState.asStateFlow()

    fun onNoteTextUpdate(text: String) {
        _uiState.update { it.copy(noteText = text) }
    }

    fun createNote(): Result<Note> {
        if (_uiState.value.noteText.isBlank()) {
            return Result.failure(Throwable("Invalid note parameters"))
        }

        return Result.success(Note(_uiState.value.noteId, text = _uiState.value.noteText))
    }
}
