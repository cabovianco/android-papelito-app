package com.cabovianco.papelito.presentation.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconButton
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.cabovianco.papelito.R
import com.cabovianco.papelito.domain.model.Note
import com.cabovianco.papelito.presentation.state.MainUiState
import com.cabovianco.papelito.presentation.ui.screen.shared.PrimaryButton
import com.cabovianco.papelito.presentation.ui.screen.shared.SecondaryButton
import com.cabovianco.papelito.presentation.ui.theme.LocalColorScheme

@Composable
fun MainScreen(
    state: MainUiState,
    onAddNoteClick: () -> Unit,
    onEditNoteClick: (Note) -> Unit,
    onDeleteNoteClick: (Note) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier) {
        Container(state, onEditNoteClick, onDeleteNoteClick)
        AddNoteButton(
            onAddNoteClick,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp)
        )
    }
}

@Composable
private fun Container(
    state: MainUiState,
    onEditNoteClick: (Note) -> Unit,
    onDeleteNoteClick: (Note) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        AppTitle()
        Spacer(modifier = Modifier.height(8.dp))
        Content(state, onEditNoteClick, onDeleteNoteClick)
    }
}

@Composable
private fun AppTitle(modifier: Modifier = Modifier) {
    val colors = LocalColorScheme.current

    Box(modifier = modifier) {
        Text(
            text = stringResource(R.string.app_name),
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = colors.onBackground
        )
    }
}

@Composable
private fun Content(
    state: MainUiState,
    onEditNoteClick: (Note) -> Unit,
    onDeleteNoteClick: (Note) -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = LocalColorScheme.current

    Column(
        modifier = modifier
    ) {
        TagsContainer()
        Spacer(modifier = Modifier.height(8.dp))

        when (state) {
            is MainUiState.Success -> NotesContainer(
                state.notes,
                onEditNoteClick,
                onDeleteNoteClick
            )

            is MainUiState.Loading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = colors.primary)
                }
            }
        }
    }
}

@Composable
private fun TagsContainer(modifier: Modifier = Modifier) {

}

@Composable
private fun NotesContainer(
    notes: List<Note>,
    onEditNoteClick: (Note) -> Unit,
    onDeleteNoteClick: (Note) -> Unit,
    modifier: Modifier = Modifier
) {
    var openDialog by remember { mutableStateOf(false) }
    var clickedNote: Note? by remember { mutableStateOf(null) }

    LazyVerticalStaggeredGrid(
        modifier = modifier,
        columns = StaggeredGridCells.Adaptive(minSize = 128.dp),
        verticalItemSpacing = 8.dp,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(bottom = 64.dp)
    ) {
        items(notes) {
            NoteItem(it, {
                openDialog = true
                clickedNote = it
            })
        }
    }

    if (openDialog && clickedNote != null) {
        NoteDialog(
            clickedNote!!,
            { openDialog = false },
            {
                onEditNoteClick(it)
                openDialog = false
            },
            {
                onDeleteNoteClick(it)
                openDialog = false
            }
        )
    }
}

@Composable
private fun NoteItem(note: Note, onClick: () -> Unit, modifier: Modifier = Modifier) {
    ElevatedCard(
        modifier = modifier,
        onClick = onClick,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
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
            text = note.text
        )
    }
}

@Composable
private fun NoteDialog(
    note: Note,
    onDismissRequest: () -> Unit,
    onEditNoteClick: (Note) -> Unit,
    onDeleteNoteClick: (Note) -> Unit,
    modifier: Modifier = Modifier
) {
    Dialog(onDismissRequest = onDismissRequest) {
        Column(modifier = modifier) {
            ElevatedCard(
                modifier = Modifier.size(width = 256.dp, height = 288.dp),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White,
                    contentColor = Color.Black
                )
            ) {
                NoteContent(note, modifier = Modifier.verticalScroll(rememberScrollState()))
            }

            NoteDialogActions({ onEditNoteClick(note) }, { onDeleteNoteClick(note) })
        }
    }
}

@Composable
private fun NoteDialogActions(
    onEditNoteClick: () -> Unit,
    onDeleteNoteClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showDeleteBottomSheet by remember { mutableStateOf(false) }

    Row(
        modifier = modifier
            .width(256.dp)
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.End
    ) {
        NoteDialogButton(onEditNoteClick) {
            Icon(painterResource(R.drawable.edit), null)
        }

        Spacer(modifier = Modifier.width(4.dp))

        NoteDialogButton({ showDeleteBottomSheet = true }) {
            Icon(painterResource(R.drawable.delete), null)
        }
    }

    if (showDeleteBottomSheet) {
        DeleteNoteBottomSheet(
            {
                onDeleteNoteClick()
                showDeleteBottomSheet = false
            },
            { showDeleteBottomSheet = false }
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

    FilledIconButton(
        modifier = modifier,
        onClick = onClick,
        colors = IconButtonDefaults.iconButtonColors(
            containerColor = colors.primary,
            contentColor = colors.onPrimary
        ),
        content = content
    )
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
                .padding(start = 16.dp, top = 16.dp, end = 16.dp)
        ) {
            Text(
                text = stringResource(R.string.delete_note_bottom_sheet_title),
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(text = stringResource(R.string.delete_note_bottom_sheet_description))

            Spacer(modifier = Modifier.height(32.dp))

            SecondaryButton(
                stringResource(R.string.cancel_button),
                onDismissRequest,
                modifier = Modifier.height(56.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            PrimaryButton(
                stringResource(R.string.continue_button),
                onAcceptRequest,
                modifier = Modifier.height(56.dp)
            )
        }
    }
}

@Composable
private fun AddNoteButton(onClick: () -> Unit, modifier: Modifier = Modifier) {
    val colors = LocalColorScheme.current

    Button(
        modifier = modifier.width(96.dp),
        onClick = onClick,
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(
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
