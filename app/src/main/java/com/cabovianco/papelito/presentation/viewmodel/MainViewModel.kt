package com.cabovianco.papelito.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cabovianco.papelito.domain.model.Note
import com.cabovianco.papelito.domain.usecase.DeleteNoteUseCase
import com.cabovianco.papelito.domain.usecase.GetAllNotesUseCase
import com.cabovianco.papelito.presentation.state.MainUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

const val STOP_TIMEOUT = 5000L

@HiltViewModel
class MainViewModel @Inject constructor(
    getAllNotesUseCase: GetAllNotesUseCase,
    private val deleteNoteUseCase: DeleteNoteUseCase
) : ViewModel() {
    val uiState = getAllNotesUseCase()
        .map { MainUiState.Success(it) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(STOP_TIMEOUT),
            initialValue = MainUiState.Loading
        )

    fun deleteNote(note: Note) {
        viewModelScope.launch { deleteNoteUseCase(note) }
    }
}
