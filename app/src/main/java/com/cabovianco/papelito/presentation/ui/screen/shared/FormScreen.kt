package com.cabovianco.papelito.presentation.ui.screen.shared

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cabovianco.papelito.R
import com.cabovianco.papelito.domain.model.NoteColor
import com.cabovianco.papelito.presentation.state.FormNoteUiState
import com.cabovianco.papelito.presentation.ui.theme.LocalColorScheme

@Composable
fun FormScreen(
    uiState: FormNoteUiState,
    onNoteTextChange: (String) -> Unit,
    onNoteBackgroundColorChange: (NoteColor) -> Unit,
    onNoteFontColorChange: (NoteColor) -> Unit,
    onCancelButtonClick: () -> Unit,
    onSaveButtonClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        NoteContainer(uiState, onNoteTextChange)

        NoteEditorContainer(
            uiState,
            onNoteBackgroundColorChange,
            onNoteFontColorChange,
            onCancelButtonClick,
            onSaveButtonClick,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun NoteContainer(
    uiState: FormNoteUiState,
    onNoteTextChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight(0.5f),
        contentAlignment = Alignment.Center
    ) {
        NoteContent(
            text = uiState.noteText,
            onTextChange = { onNoteTextChange(it) },
            backgroundColor = uiState.noteBackgroundColor,
            fontColor = uiState.noteFontColor
        )
    }
}

@Composable
private fun NoteContent(
    text: String,
    onTextChange: (String) -> Unit,
    backgroundColor: NoteColor,
    fontColor: NoteColor,
    modifier: Modifier = Modifier
) {
    ElevatedCard(
        modifier = modifier.fillMaxSize(0.8f),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor.value),
        elevation = CardDefaults.elevatedCardElevation(2.dp)
    ) {
        BasicTextField(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            value = text,
            onValueChange = { onTextChange(it) },
            textStyle = TextStyle(color = fontColor.value, fontSize = 16.sp),
            cursorBrush = SolidColor(fontColor.value)
        )
    }
}

@Composable
private fun NoteEditorContainer(
    uiState: FormNoteUiState,
    onNoteBackgroundColorChange: (NoteColor) -> Unit,
    onNoteFontColorChange: (NoteColor) -> Unit,
    onCancelButtonClick: () -> Unit,
    onSaveButtonClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = LocalColorScheme.current
    val shape = RoundedCornerShape(16.dp)

    Column(
        modifier = modifier
            .fillMaxWidth()
            .shadow(elevation = 2.dp, shape = shape)
            .background(color = colors.surface, shape = shape),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        NoteEditorContent(
            uiState,
            onNoteBackgroundColorChange,
            onNoteFontColorChange,
            modifier = Modifier.weight(1f)
        )

        NoteEditorActions(onCancelButtonClick, onSaveButtonClick)
    }
}

private enum class EditMode { Background, Font }

@Composable
private fun NoteEditorContent(
    uiState: FormNoteUiState,
    onNoteBackgroundColorChange: (NoteColor) -> Unit,
    onNoteFontColorChange: (NoteColor) -> Unit,
    modifier: Modifier = Modifier
) {
    var editMode by remember { mutableStateOf(EditMode.Background) }

    val title = stringResource(when (editMode) {
        EditMode.Background -> R.string.background_color_note_property
        EditMode.Font -> R.string.font_color_note_property
    })

    val selected = when (editMode) {
        EditMode.Background -> uiState.noteBackgroundColor
        EditMode.Font -> uiState.noteFontColor
    }

    val onSelectedChange = when (editMode) {
        EditMode.Background -> onNoteBackgroundColorChange
        EditMode.Font -> onNoteFontColorChange
    }

    Column(modifier = modifier) {
        SelectNoteColor(
            title = title,
            selected,
            onSelectedChange = { onSelectedChange(it) },
            selectionButtonIcon = {
                Icon(
                    painter = painterResource(
                        if (editMode == EditMode.Background) R.drawable.font
                        else R.drawable.paint_bucket
                    ),
                    contentDescription = null
                )
            },
            onSelectionButtonClick = {
                editMode = when (editMode) {
                    EditMode.Background -> EditMode.Font
                    EditMode.Font -> EditMode.Background
                }
            }
        )
    }
}

@Composable
private fun NoteEditorActions(
    onCancelButtonClick: () -> Unit,
    onSaveButtonClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        CancelButton(
            modifier = modifier
                .height(48.dp)
                .weight(0.5f),
            title = stringResource(R.string.cancel_button),
            onClick = onCancelButtonClick
        )

        Spacer(modifier = Modifier.width(16.dp))

        ConfirmButton(
            modifier = modifier
                .height(48.dp)
                .weight(0.5f),
            title = stringResource(R.string.save_button),
            onClick = onSaveButtonClick
        )
    }
}

@Composable
private fun NotePropertyContainer(
    title: String,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    val colors = LocalColorScheme.current

    Column(
        modifier = modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = title,
            color = colors.onSurface,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
        )

        content()
    }
}

@Composable
private fun SelectNoteColor(
    title: String,
    selected: NoteColor,
    onSelectedChange: (NoteColor) -> Unit,
    selectionButtonIcon: @Composable RowScope.() -> Unit,
    onSelectionButtonClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = LocalColorScheme.current

    NotePropertyContainer(
        modifier = modifier,
        title = title
    ) {
        Row(
            modifier = Modifier.horizontalScroll(rememberScrollState()),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            NoteColor.entries.forEach {
                SelectNoteColorItem(color = it, selected, onSelectedChange)
            }

            Button(
                onClick = onSelectionButtonClick,
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = colors.secondary,
                    contentColor = colors.onSecondary
                ),
                content = selectionButtonIcon
            )
        }
    }
}

@Composable
private fun SelectNoteColorItem(
    color: NoteColor,
    selected: NoteColor,
    onSelectedChange: (NoteColor) -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = LocalColorScheme.current

    Button(
        modifier = modifier.size(40.dp),
        onClick = { onSelectedChange(color) },
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = color.value,
            contentColor = colors.onSurface
        ),
        contentPadding = PaddingValues(0.dp)
    ) {
        if (selected == color) {
            Icon(painter = painterResource(R.drawable.check), contentDescription = null)
        }
    }
}
