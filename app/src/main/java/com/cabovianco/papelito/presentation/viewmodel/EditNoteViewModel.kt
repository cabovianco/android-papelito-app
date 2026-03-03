package com.cabovianco.papelito.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import com.cabovianco.papelito.domain.model.isErr
import com.cabovianco.papelito.domain.model.unwrap
import com.cabovianco.papelito.domain.usecase.ObserveNoteByIdUseCase
import com.cabovianco.papelito.domain.usecase.UpdateNoteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class EditNoteViewModel @Inject constructor(
    private val observeNoteByIdUseCase: ObserveNoteByIdUseCase,
    private val updateNoteUseCase: UpdateNoteUseCase
) : FormNoteViewModel() {
    private val _id: MutableStateFlow<Long> = MutableStateFlow(0L)

    fun loadNoteById(id: Long) {
        viewModelScope.launch {
            observeNoteByIdUseCase(id).collect {
                if (it.isErr()) {
                    return@collect
                }

                _id.value = id

                mutableUiState.update { state ->
                    with(it.unwrap()) {
                        state.copy(
                            text = text,
                            backgroundColor = backgroundColor,
                            fontColor = fontColor,
                            fontSize = fontSize,
                            fontWeight = fontWeight,
                            fontFamily = fontFamily
                        )
                    }
                }
            }
        }
    }

    fun editNote() {
        super.saveNote { updateNoteUseCase(it.copy(id = _id.value)) }
    }
}
