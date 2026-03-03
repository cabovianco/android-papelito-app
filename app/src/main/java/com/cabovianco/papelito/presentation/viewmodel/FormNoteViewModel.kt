package com.cabovianco.papelito.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cabovianco.papelito.domain.model.AppError
import com.cabovianco.papelito.domain.model.Result
import com.cabovianco.papelito.domain.model.err
import com.cabovianco.papelito.domain.model.note.Note
import com.cabovianco.papelito.domain.model.note.NoteColor
import com.cabovianco.papelito.domain.model.note.NoteFontFamily
import com.cabovianco.papelito.domain.model.note.NoteFontWeight
import com.cabovianco.papelito.domain.model.ok
import com.cabovianco.papelito.domain.model.onErr
import com.cabovianco.papelito.domain.model.onOk
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
        mutableUiState.update { it.copy(text = text) }
    }

    fun onNoteBackgroundColorUpdate(backgroundColor: NoteColor) {
        mutableUiState.update { it.copy(backgroundColor = backgroundColor) }
    }

    fun onNoteFontColorUpdate(fontColor: NoteColor) {
        mutableUiState.update { it.copy(fontColor = fontColor) }
    }

    fun onNoteFontSizeUpdate(fontSize: Float) {
        if (fontSize <= 0f || fontSize > 99f) return

        mutableUiState.update { it.copy(fontSize = fontSize) }
    }

    fun onNoteFontWeightUpdate(fontWeight: NoteFontWeight) {
        mutableUiState.update { it.copy(fontWeight = fontWeight) }
    }

    fun onNoteFontFamilyUpdate(fontFamily: NoteFontFamily) {
        mutableUiState.update { it.copy(fontFamily = fontFamily) }
    }

    private fun createNote(): Result<Note> {
        if (mutableUiState.value.text.isBlank()) {
            return err(AppError.InvalidNoteParametersError)
        }

        return ok(with(mutableUiState.value) {
            Note(
                text = text,
                backgroundColor = backgroundColor,
                fontColor = fontColor,
                fontSize = fontSize,
                fontWeight = fontWeight,
                fontFamily = fontFamily
            )
        })
    }

    protected fun saveNote(saveNoteUseCase: suspend (Note) -> Result<Unit>) {
        viewModelScope.launch {
            createNote()
                .onOk { note ->
                    saveNoteUseCase(note)
                        .onOk { mutableUiEvent.emit(FormNoteUiEvent.Saved) }
                        .onErr { mutableUiEvent.emit(FormNoteUiEvent.Error(it)) }
                }
                .onErr { mutableUiEvent.emit(FormNoteUiEvent.Error(it)) }
        }
    }
}
