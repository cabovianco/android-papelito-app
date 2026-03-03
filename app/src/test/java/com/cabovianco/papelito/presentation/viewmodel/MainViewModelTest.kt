package com.cabovianco.papelito.presentation.viewmodel

import com.cabovianco.papelito.domain.model.AppError
import com.cabovianco.papelito.domain.model.Result
import com.cabovianco.papelito.domain.usecase.DeleteNoteUseCase
import com.cabovianco.papelito.domain.usecase.ObserveAllNotesUseCase
import com.cabovianco.papelito.presentation.state.MainUiState
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

class MainViewModelTest {
    @get:Rule
    val dispatcherRule = MainDispatcherRule()

    private val observeAllNotesUseCase = mockk<ObserveAllNotesUseCase>()
    private val deleteNoteUseCase = mockk<DeleteNoteUseCase>()

    @Test
    fun initialState_isLoading() {
        coEvery { observeAllNotesUseCase.invoke() } returns
                flowOf(Result.Ok(listOf()))

        val viewModel = MainViewModel(observeAllNotesUseCase, deleteNoteUseCase)

        assert(viewModel.uiState.value is MainUiState.Loading)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun observeAllNotes_returnsSuccess() = runTest {
        coEvery { observeAllNotesUseCase.invoke() } returns
                flowOf(Result.Ok(listOf()))

        val viewModel = MainViewModel(observeAllNotesUseCase, deleteNoteUseCase)

        val job = launch { viewModel.uiState.collect {} }
        advanceUntilIdle()

        assert(viewModel.uiState.value is MainUiState.Success)

        job.cancel()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun observeAllNotes_returnsError() = runTest {
        coEvery { observeAllNotesUseCase.invoke() } returns
                flowOf(Result.Err(AppError.DatabaseError.Unknown))

        val viewModel = MainViewModel(observeAllNotesUseCase, deleteNoteUseCase)

        val job = launch { viewModel.uiState.collect { } }
        advanceUntilIdle()

        assert(viewModel.uiState.value is MainUiState.Error)

        job.cancel()
    }
}
