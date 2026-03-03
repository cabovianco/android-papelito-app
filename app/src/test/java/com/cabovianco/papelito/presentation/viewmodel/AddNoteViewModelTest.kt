package com.cabovianco.papelito.presentation.viewmodel

import com.cabovianco.papelito.domain.model.AppError
import com.cabovianco.papelito.domain.model.Result
import com.cabovianco.papelito.domain.usecase.AddNoteUseCase
import com.cabovianco.papelito.presentation.event.FormNoteUiEvent
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

class AddNoteViewModelTest {
    @get:Rule
    val dispatcherRule = MainDispatcherRule()

    private val addNoteUseCase = mockk<AddNoteUseCase>()

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun emitSaved_whenValidNoteIsSaved() = runTest {
        coEvery { addNoteUseCase.invoke(any()) } returns Result.Ok(Unit)

        val viewModel = AddNoteViewModel(addNoteUseCase)

        var event: FormNoteUiEvent? = null
        val job = launch(UnconfinedTestDispatcher()) {
            viewModel.uiEvent.collect { event = it }
        }

        viewModel.onNoteTextUpdate("Text")
        viewModel.addNote()

        assert(event is FormNoteUiEvent.Saved)

        job.cancel()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun emitError_whenInvalidNoteIsSaved() = runTest {
        coEvery { addNoteUseCase.invoke(any()) } returns
                Result.Err(AppError.InvalidNoteParametersError)

        val viewModel = AddNoteViewModel(addNoteUseCase)

        var event: FormNoteUiEvent? = null
        val job = launch(UnconfinedTestDispatcher()) {
            viewModel.uiEvent.collect { event = it }
        }

        viewModel.addNote()

        assert(event is FormNoteUiEvent.Error)

        job.cancel()
    }
}
