package com.cabovianco.papelito.presentation.viewmodel

import com.cabovianco.papelito.domain.usecase.AddNoteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject

@HiltViewModel
class AddNoteViewModel @Inject constructor(
    private val addNoteUseCase: AddNoteUseCase
) : FormNoteViewModel() {
    fun onSaveButtonClick() {
        super.saveNote { addNoteUseCase(it) }
    }
}
