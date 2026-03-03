package com.cabovianco.papelito.presentation.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cabovianco.papelito.R
import com.cabovianco.papelito.domain.model.note.Note
import com.cabovianco.papelito.presentation.state.MainUiState
import com.cabovianco.papelito.presentation.ui.screen.shared.PrimaryButton
import com.cabovianco.papelito.presentation.ui.screen.shared.SecondaryButton
import com.cabovianco.papelito.presentation.ui.theme.Fascinate
import com.cabovianco.papelito.presentation.ui.theme.LocalColorScheme
import com.cabovianco.papelito.presentation.viewmodel.MainViewModel

@Composable
fun MainScreen(
    viewModel: MainViewModel,
    onAddButtonClick: () -> Unit,
    onEditNoteClick: (Long) -> Unit,
    onDeleteNoteClick: (Note) -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(R.drawable.background),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        MainScreenContent(uiState, onAddButtonClick, onEditNoteClick, onDeleteNoteClick, modifier)
    }
}

@Composable
fun MainScreenContent(
    uiState: MainUiState,
    onAddButtonClick: () -> Unit,
    onEditNoteClick: (Long) -> Unit,
    onDeleteNoteClick: (Note) -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = LocalColorScheme.current

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = { AppBar() },
        floatingActionButton = { AddNoteButton(onClick = onAddButtonClick) },
        floatingActionButtonPosition = FabPosition.Center,
        containerColor = Color.Transparent,
        contentColor = Color.Transparent
    ) {
        val modifier = Modifier.padding(it)

        when (uiState) {
            is MainUiState.Success -> {
                if (uiState.notes.isEmpty()) {
                    ScreenStateContent(modifier) {
                        Text(
                            text = stringResource(R.string.notes_list_empty_message),
                            color = colors.onBackground,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                } else {
                    NotesListContent(
                        uiState,
                        onEditNoteClick,
                        onDeleteNoteClick,
                        modifier
                    )
                }
            }

            is MainUiState.Error -> ScreenStateContent(modifier) {
                Text(
                    text = stringResource(R.string.load_error_message),
                    color = colors.onBackground,
                    fontSize = 15.sp
                )
            }

            is MainUiState.Loading -> ScreenStateContent(modifier) {
                CircularProgressIndicator(color = colors.primary)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AppBar(modifier: Modifier = Modifier) {
    val colors = LocalColorScheme.current

    CenterAlignedTopAppBar(
        modifier = modifier,
        title = {
            Text(
                text = stringResource(R.string.app_name),
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = Fascinate
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Transparent,
            titleContentColor = colors.onBackground
        )
    )
}

@Composable
private fun NotesListContent(
    uiState: MainUiState.Success,
    onEditNoteClick: (Long) -> Unit,
    onDeleteNoteClick: (Note) -> Unit,
    modifier: Modifier = Modifier
) {
    var clickedNote by remember { mutableStateOf<Note?>(null) }

    LazyVerticalStaggeredGrid(
        modifier = modifier,
        columns = StaggeredGridCells.Adaptive(minSize = 128.dp),
        verticalItemSpacing = 8.dp,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(start = 16.dp, top = 16.dp, end = 16.dp, bottom = 80.dp)
    ) {
        items(uiState.notes) { note ->
            NoteItem(note, onClick = { clickedNote = it })
        }
    }

    clickedNote?.let { note ->
        NotePreviewDialog(
            note,
            onEditNoteClick = {
                onEditNoteClick(note.id)
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
            fontSize = note.fontSize.sp,
            fontWeight = note.fontWeight.value,
            fontFamily = note.fontFamily.value,
            color = note.fontColor.value
        )
    }
}

@Composable
private fun NotePreviewDialog(
    note: Note,
    onEditNoteClick: () -> Unit,
    onDeleteNoteClick: () -> Unit,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier
) {
    Dialog(onDismissRequest = onDismissRequest) {
        Column(modifier = modifier) {
            ElevatedCard(
                modifier = Modifier.size(288.dp),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = note.backgroundColor.value)
            ) {
                NoteContent(note, modifier = Modifier.verticalScroll(rememberScrollState()))
            }

            Box(modifier = Modifier.width(288.dp)) {
                NoteActionsRow(onEditNoteClick, onDeleteNoteClick, modifier = Modifier)
            }
        }
    }
}

@Composable
private fun NoteActionsRow(
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
        NoteActionButton(onClick = onEditNoteClick) {
            Icon(painter = painterResource(R.drawable.edit_button), contentDescription = null)
        }

        NoteActionButton(onClick = { deleteButtonClicked = true }) {
            Icon(painter = painterResource(R.drawable.delete_button), contentDescription = null)
        }
    }

    if (deleteButtonClicked) {
        DeleteNoteSheet(
            onAcceptRequest = {
                onDeleteNoteClick()
                deleteButtonClicked = false
            },
            onDismissRequest = { deleteButtonClicked = false }
        )
    }
}

@Composable
private fun NoteActionButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    val colors = LocalColorScheme.current
    val shape = RoundedCornerShape(16.dp)

    ElevatedButton(
        modifier = modifier
            .size(48.dp)
            .shadow(
                elevation = 1.dp,
                shape = shape,
                clip = false
            ),
        onClick = onClick,
        shape = shape,
        colors = ButtonDefaults.elevatedButtonColors(
            containerColor = colors.primary,
            contentColor = colors.onPrimary
        ),
        contentPadding = PaddingValues(0.dp)
    ) {
        content()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DeleteNoteSheet(
    onAcceptRequest: () -> Unit,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = LocalColorScheme.current

    ModalBottomSheet(
        modifier = modifier
            .systemBarsPadding()
            .padding(16.dp),
        onDismissRequest = onDismissRequest,
        shape = RoundedCornerShape(24.dp),
        containerColor = colors.surface,
        contentColor = colors.onSurface,
        dragHandle = null,
        contentWindowInsets = { WindowInsets(bottom = 0.dp) }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            DeleteNoteMessage()

            DeleteNoteActions(onAcceptRequest, onDismissRequest)
        }
    }
}

@Composable
private fun DeleteNoteMessage(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = stringResource(R.string.delete_note_title),
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold
        )

        Text(text = stringResource(R.string.delete_note_description))
    }
}

@Composable
private fun DeleteNoteActions(
    onAcceptRequest: () -> Unit,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier
) {
    val buttonModifier = Modifier.height(60.dp)

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        SecondaryButton(
            title = stringResource(R.string.action_cancel),
            onClick = onDismissRequest,
            modifier = buttonModifier
        )

        PrimaryButton(
            title = stringResource(R.string.action_continue),
            onClick = onAcceptRequest,
            modifier = buttonModifier
        )
    }
}

@Composable
private fun ScreenStateContent(
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
        content = content
    )
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
            painter = painterResource(R.drawable.add_button),
            contentDescription = null,
            tint = colors.onPrimary
        )
    }
}
