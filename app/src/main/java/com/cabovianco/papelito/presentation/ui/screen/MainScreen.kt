package com.cabovianco.papelito.presentation.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.cabovianco.papelito.R
import com.cabovianco.papelito.domain.model.Note
import com.cabovianco.papelito.presentation.navigation.Screen
import com.cabovianco.papelito.presentation.state.MainUiState
import com.cabovianco.papelito.presentation.ui.screen.shared.CancelButton
import com.cabovianco.papelito.presentation.ui.screen.shared.ConfirmButton
import com.cabovianco.papelito.presentation.ui.theme.Fascinate
import com.cabovianco.papelito.presentation.ui.theme.LocalColorScheme
import com.cabovianco.papelito.presentation.viewmodel.MainViewModel

@Composable
fun MainScreen(
    viewmodel: MainViewModel,
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    val uiState by viewmodel.uiState.collectAsStateWithLifecycle()

    Box(modifier = modifier) {
        MainContainer(
            uiState,
            onEditNoteClick = { navController.navigate(Screen.EditNoteScreen.navTo(it.id)) },
            onDeleteNoteClick = { viewmodel.deleteNote(it) }
        )

        AddNoteButton(
            onClick = { navController.navigate(Screen.AddNoteScreen.route) },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp)
        )
    }
}

@Composable
private fun MainContainer(
    uiState: MainUiState,
    onEditNoteClick: (Note) -> Unit,
    onDeleteNoteClick: (Note) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        AppBar(modifier = Modifier.padding(vertical = 16.dp))
        MainContent(uiState, onEditNoteClick, onDeleteNoteClick)
    }
}

@Composable
private fun AppBar(modifier: Modifier = Modifier) {
    val colors = LocalColorScheme.current

    Box(modifier = modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
        Text(
            text = stringResource(R.string.app_name),
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = Fascinate,
            color = colors.onBackground
        )
    }
}

@Composable
private fun MainContent(
    uiState: MainUiState,
    onEditNoteClick: (Note) -> Unit,
    onDeleteNoteClick: (Note) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        when (uiState) {
            is MainUiState.Success -> {
                NotesContainer(
                    uiState,
                    onEditNoteClick,
                    onDeleteNoteClick,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }

            is MainUiState.Loading -> {
                LoadingContainer()
            }
        }
    }
}

@Composable
private fun NotesContainer(
    uiState: MainUiState.Success,
    onEditNoteClick: (Note) -> Unit,
    onDeleteNoteClick: (Note) -> Unit,
    modifier: Modifier = Modifier
) {
    var clickedNote by remember { mutableStateOf<Note?>(null) }

    NotesGrid(
        modifier = modifier,
        notes = uiState.notes,
        onNoteClicked = { clickedNote = it },
    )

    clickedNote?.let { note ->
        NoteDialog(
            note,
            onEditNoteClick = {
                onEditNoteClick(note)
                clickedNote = null
            },
            onDeleteNoteClick = {
                onDeleteNoteClick(note)
                clickedNote = null
            },
            onDismissRequest = { clickedNote = null }
        )
    }
}

@Composable
private fun NotesGrid(
    notes: List<Note>,
    onNoteClicked: (Note) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyVerticalStaggeredGrid(
        modifier = modifier,
        columns = StaggeredGridCells.Adaptive(minSize = 128.dp),
        verticalItemSpacing = 8.dp,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(bottom = 64.dp)
    ) {
        items(notes) {
            NoteItem(note = it, onClick = onNoteClicked)
        }
    }
}

@Composable
private fun NoteItem(note: Note, onClick: (Note) -> Unit, modifier: Modifier = Modifier) {
    ElevatedCard(
        modifier = modifier,
        onClick = { onClick(note) },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = note.backgroundColor.value)
    ) {
        NoteContent(note)
    }
}

@Composable
private fun NoteContent(note: Note, modifier: Modifier = Modifier) {
    Box(modifier = modifier) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            text = note.text,
            fontSize = 15.sp,
            color = note.fontColor.value
        )
    }
}

@Composable
private fun NoteDialog(
    note: Note,
    onEditNoteClick: () -> Unit,
    onDeleteNoteClick: () -> Unit,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier
) {
    val size = 288.dp

    Dialog(onDismissRequest = onDismissRequest) {
        Column(modifier = modifier) {
            ElevatedCard(
                modifier = Modifier.size(size),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = note.backgroundColor.value)
            ) {
                NoteContent(note, modifier = Modifier.verticalScroll(rememberScrollState()))
            }

            Box(modifier = Modifier.width(size)) {
                NoteDialogActions(onEditNoteClick, onDeleteNoteClick, modifier = Modifier)
            }
        }
    }
}

@Composable
private fun NoteDialogActions(
    onEditNoteClick: () -> Unit,
    onDeleteNoteClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var deleteButtonClicked by remember { mutableStateOf(false) }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.End)
    ) {
        NoteDialogButton(onClick = onEditNoteClick) {
            Icon(painter = painterResource(R.drawable.edit), contentDescription = null)
        }

        NoteDialogButton(onClick = { deleteButtonClicked = true }) {
            Icon(painter = painterResource(R.drawable.delete), contentDescription = null)
        }
    }

    if (deleteButtonClicked) {
        DeleteNoteBottomSheet(
            onAcceptRequest = {
                onDeleteNoteClick()
                deleteButtonClicked = false
            },
            onDismissRequest = { deleteButtonClicked = false }
        )
    }
}

@Composable
private fun NoteDialogButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    val colors = LocalColorScheme.current
    val shape = RoundedCornerShape(16.dp)

    FilledTonalIconButton(
        modifier = modifier
            .size(48.dp)
            .shadow(
                elevation = 1.dp,
                shape = shape,
                clip = false
            ),
        onClick = onClick,
        shape = shape,
        colors = IconButtonDefaults.iconButtonColors(
            containerColor = colors.primary,
            contentColor = colors.onPrimary
        )
    ) {
        content()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DeleteNoteBottomSheet(
    onAcceptRequest: () -> Unit,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = LocalColorScheme.current

    ModalBottomSheet(
        modifier = modifier.padding(16.dp),
        onDismissRequest = onDismissRequest,
        shape = RoundedCornerShape(24.dp),
        containerColor = colors.surface,
        contentColor = colors.onSurface,
        dragHandle = null
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, top = 16.dp, end = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            DeleteNoteBottomSheetInfo()
            DeleteNoteBottomSheetActions(onAcceptRequest, onDismissRequest)
        }
    }
}

@Composable
private fun DeleteNoteBottomSheetInfo(modifier: Modifier = Modifier) {
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text(
            text = stringResource(R.string.delete_note_title),
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold
        )

        Text(text = stringResource(R.string.delete_note_description))
    }
}

@Composable
private fun DeleteNoteBottomSheetActions(
    onAcceptRequest: () -> Unit,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(16.dp)) {
        CancelButton(
            title = stringResource(R.string.action_cancel),
            onClick = onDismissRequest,
            modifier = Modifier.height(60.dp)
        )

        ConfirmButton(
            title = stringResource(R.string.action_continue),
            onClick = onAcceptRequest,
            modifier = Modifier.height(60.dp)
        )
    }
}

@Composable
private fun LoadingContainer(modifier: Modifier = Modifier) {
    val colors = LocalColorScheme.current

    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator(color = colors.primary)
    }
}

@Composable
private fun AddNoteButton(onClick: () -> Unit, modifier: Modifier = Modifier) {
    val colors = LocalColorScheme.current

    ElevatedButton(
        modifier = modifier.size(96.dp, 48.dp),
        onClick = onClick,
        shape = RoundedCornerShape(18.dp),
        colors = ButtonDefaults.elevatedButtonColors(
            containerColor = colors.primary,
            contentColor = colors.onPrimary
        )
    ) {
        Icon(
            painter = painterResource(R.drawable.add),
            contentDescription = null,
            tint = colors.onPrimary
        )
    }
}
