package com.cabovianco.papelito.presentation.viewmodel

import com.cabovianco.papelito.domain.model.Result
import com.cabovianco.papelito.domain.model.note.Note
import com.cabovianco.papelito.domain.model.note.NoteColor
import com.cabovianco.papelito.domain.model.note.NoteFontFamily
import com.cabovianco.papelito.domain.model.note.NoteFontWeight
import com.cabovianco.papelito.domain.usecase.ObserveNoteByIdUseCase
import com.cabovianco.papelito.domain.usecase.UpdateNoteUseCase
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import org.junit.Rule
import org.junit.Test

class EditNoteViewModelTest {
    @get:Rule
    val dispatcherRule = MainDispatcherRule()

    private val observeNoteByIdUseCase = mockk<ObserveNoteByIdUseCase>()
    private val updateNoteUseCase = mockk<UpdateNoteUseCase>()

    @Test
    fun loadNoteById_updatesUiStateWithNoteData() {
        coEvery { observeNoteByIdUseCase.invoke(any()) } returns flowOf(
            Result.Ok(
                Note(
                    id = 1L,
                    text = "Text",
                    backgroundColor = NoteColor.WHITE,
                    fontColor = NoteColor.BLACK,
                    fontSize = 16F,
                    fontWeight = NoteFontWeight.NORMAL,
                    fontFamily = NoteFontFamily.SANS_SERIF
                )
            )
        )

        val viewModel = EditNoteViewModel(observeNoteByIdUseCase, updateNoteUseCase)

        viewModel.loadNoteById(1L)

        assert(viewModel.uiState.value.text == "Text")
    }
}
