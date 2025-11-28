package com.cabovianco.papelito.presentation.ui.screen

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.cabovianco.papelito.presentation.ui.screen.shared.FormScreen

@Composable
fun AddNoteScreen(
    onNavigateBackButtonClick: () -> Unit,
    noteTextValue: String,
    onNoteTextChange: (String) -> Unit,
    onCancelButtonClick: () -> Unit,
    onSaveButtonClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    FormScreen(
        onNavigateBackButtonClick,
        noteTextValue,
        onNoteTextChange,
        onCancelButtonClick,
        onSaveButtonClick,
        modifier
    )
}
