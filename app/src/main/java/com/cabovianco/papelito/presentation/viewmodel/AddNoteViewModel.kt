package com.cabovianco.papelito.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import com.cabovianco.papelito.domain.usecase.AddNoteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class AddNoteViewModel @Inject constructor(
    private val addNoteUseCase: AddNoteUseCase
) : FormNoteViewModel() {
    fun onSaveButtonClick(): Result<Unit> {
        super.createNote()
            .onSuccess { viewModelScope.launch { addNoteUseCase(it) } }
            .onFailure { return Result.failure(it) }

        return Result.success(Unit)
    }
}
