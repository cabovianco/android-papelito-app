package com.cabovianco.papelito.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import com.cabovianco.papelito.domain.usecase.GetNoteByIdUseCase
import com.cabovianco.papelito.domain.usecase.UpdateNoteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class EditNoteViewModel @Inject constructor(
    private val getNoteByIdUseCase: GetNoteByIdUseCase,
    private val updateNoteUseCase: UpdateNoteUseCase
) : FormNoteViewModel() {
    fun editNoteById(id: Long) {
        viewModelScope.launch {
            getNoteByIdUseCase(id).collect { note ->
                _uiState.update { it.copy(id, note?.text ?: "") }
            }
        }
    }

    fun onSaveButtonClick(): Result<Unit> {
        createNote()
            .onSuccess { viewModelScope.launch { updateNoteUseCase(it) } }
            .onFailure { return Result.failure(it) }

        return Result.success(Unit)
    }
}
