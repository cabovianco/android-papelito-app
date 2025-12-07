package com.cabovianco.papelito.presentation.ui.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.cabovianco.papelito.presentation.event.FormNoteUiEvent
import com.cabovianco.papelito.presentation.ui.screen.shared.FormScreen
import com.cabovianco.papelito.presentation.viewmodel.AddNoteViewModel

@Composable
fun AddNoteScreen(
    viewmodel: AddNoteViewModel,
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    val uiState by viewmodel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewmodel.uiEvent.collect { event ->
            when (event) {
                is FormNoteUiEvent.Saved -> navController.navigateUp()
                is FormNoteUiEvent.Error -> {}
            }
        }
    }

    FormScreen(
        uiState,
        { viewmodel.onNoteTextUpdate(it) },
        { viewmodel.onNoteBackgroundColorUpdate(it) },
        { viewmodel.onNoteFontColorUpdate(it) },
        { navController.navigateUp() },
        { viewmodel.onSaveButtonClick() },
        modifier,
    )
}
