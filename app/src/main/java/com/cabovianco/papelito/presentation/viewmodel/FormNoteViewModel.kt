package com.cabovianco.papelito.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cabovianco.papelito.domain.model.Note
import com.cabovianco.papelito.domain.model.NoteColor
import com.cabovianco.papelito.domain.model.NoteFontFamily
import com.cabovianco.papelito.domain.model.NoteFontWeight
import com.cabovianco.papelito.presentation.event.FormNoteUiEvent
import com.cabovianco.papelito.presentation.state.FormNoteUiState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

open class FormNoteViewModel : ViewModel() {
    protected val mutableUiState: MutableStateFlow<FormNoteUiState> =
        MutableStateFlow(FormNoteUiState())
    val uiState get() = mutableUiState.asStateFlow()

    protected val mutableUiEvent: MutableSharedFlow<FormNoteUiEvent> = MutableSharedFlow()
    val uiEvent get() = mutableUiEvent.asSharedFlow()

    fun onNoteTextUpdate(text: String) {
        mutableUiState.update { it.copy(noteText = text) }
    }

    fun onNoteBackgroundColorUpdate(backgroundColor: NoteColor) {
        mutableUiState.update { it.copy(noteBackgroundColor = backgroundColor) }
    }

    fun onNoteFontColorUpdate(fontColor: NoteColor) {
        mutableUiState.update { it.copy(noteFontColor = fontColor) }
    }

    fun onNoteFontSizeUpdate(fontSize: Float) {
        if (fontSize <= 0f || fontSize > 99f) return

        mutableUiState.update { it.copy(noteFontSize = fontSize) }
    }

    fun onNoteFontWeightUpdate(fontWeight: NoteFontWeight) {
        mutableUiState.update { it.copy(noteFontWeight = fontWeight) }
    }

    fun onNoteFontFamilyUpdate(fontFamily: NoteFontFamily) {
        mutableUiState.update { it.copy(noteFontFamily = fontFamily) }
    }

    protected fun createNote(): Result<Note> {
        if (mutableUiState.value.noteText.isBlank()) {
            return Result.failure(Throwable("Invalid note parameters."))
        }

        return Result.success(with(mutableUiState.value) {
            Note(noteId, noteText, noteBackgroundColor, noteFontColor, noteFontSize, noteFontWeight, noteFontFamily)
        })
    }

    protected fun saveNote(saveNoteUseCase: suspend (Note) -> Result<Unit>) {
        viewModelScope.launch {
            createNote()
                .onSuccess { note ->
                    saveNoteUseCase(note)
                        .onSuccess { mutableUiEvent.emit(FormNoteUiEvent.Saved) }
                        .onFailure { mutableUiEvent.emit(FormNoteUiEvent.Error(it.message)) }
                }
                .onFailure { mutableUiEvent.emit(FormNoteUiEvent.Error(it.message)) }
        }
    }
}
