package com.cabovianco.papelito.presentation.ui.screen.shared

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
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
                .padding(horizontal = 16.dp)
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
            .fillMaxHeight(0.45f),
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
        modifier = modifier.size(288.dp),
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
    Column(
        modifier = modifier.fillMaxWidth(),
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

@Composable
private fun NoteEditorContent(
    uiState: FormNoteUiState,
    onNoteBackgroundColorChange: (NoteColor) -> Unit,
    onNoteFontColorChange: (NoteColor) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        ColorPropertyContainer(uiState, onNoteBackgroundColorChange, onNoteFontColorChange)
    }
}

@Composable
private fun ColorPropertyContainer(
    uiState: FormNoteUiState,
    onNoteBackgroundColorChange: (NoteColor) -> Unit,
    onNoteFontColorChange: (NoteColor) -> Unit,
    modifier: Modifier = Modifier
) {
    NotePropertyContainer(
        title = stringResource(R.string.color_selection_title),
        items = listOf(
            backgroundColorPropertyItem(color = uiState.noteBackgroundColor, onColorChange = onNoteBackgroundColorChange),
            fontColorPropertyItem(color = uiState.noteFontColor, onColorChange = onNoteFontColorChange)
        ),
        modifier
    )
}

@Composable
private fun backgroundColorPropertyItem(color: NoteColor, onColorChange: (NoteColor) -> Unit) =
    PropertyItem(label = stringResource(R.string.background_color_label)) {
        ColorPicker(color, onColorChange)
    }

@Composable
private fun fontColorPropertyItem(color: NoteColor, onColorChange: (NoteColor) -> Unit) =
    PropertyItem(label = stringResource(R.string.font_color_label)) {
        ColorPicker(color, onColorChange)
    }

@Composable
private fun ColorPicker(color: NoteColor, onColorChange: (NoteColor) -> Unit) {
    var clicked by remember { mutableStateOf(false) }

    ColorPickerItem(color, onClick = { clicked = true }, modifier = Modifier.size(32.dp))

    if (clicked) {
        ColorPickerDialog(color, onColorChange, onDismissRequest = { clicked = false })
    }
}

@Composable
private fun ColorPickerItem(color: NoteColor, onClick: () -> Unit, modifier: Modifier = Modifier) {
    ElevatedButton(
        modifier = modifier,
        onClick = onClick,
        shape = CircleShape,
        colors = ButtonDefaults.buttonColors(containerColor = color.value)
    ) { }
}

@Composable
private fun ColorPickerDialog(
    color: NoteColor,
    onColorChange: (NoteColor) -> Unit,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = LocalColorScheme.current

    Dialog(onDismissRequest = onDismissRequest) {
        LazyVerticalGrid(
            modifier = modifier
                .width(256.dp)
                .background(color = colors.surface, shape = RoundedCornerShape(16.dp))
                .padding(14.dp),
            columns = GridCells.FixedSize(48.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
            contentPadding = PaddingValues(2.dp)
        ) {
            items(NoteColor.entries) {
                ColorPickerItem(
                    color = it,
                    onClick = { onColorChange(it) },
                    modifier = Modifier
                        .size(48.dp)
                        .then(
                            if (it == color) Modifier
                                .padding(4.dp)
                                .border(
                                    border = BorderStroke(width = 2.dp, color = colors.onSurface),
                                    shape = CircleShape
                                )
                            else Modifier
                        )
                )
            }
        }
    }
}

private data class PropertyItem(val label: String, val content: @Composable () -> Unit)

@Composable
private fun NotePropertyContainer(
    title: String,
    items: List<PropertyItem>,
    modifier: Modifier = Modifier
) {
    val colors = LocalColorScheme.current

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = title,
            color = colors.onSurface,
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold
        )

        ElevatedCard(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = colors.surface,
                contentColor = colors.onSurface
            )
        ) {
            items.forEachIndexed { index, item ->
                NotePropertyItem(item)

                if (index != items.lastIndex) {
                    HorizontalDivider(modifier = Modifier.fillMaxWidth(), color = colors.background)
                }
            }
        }
    }
}

@Composable
private fun NotePropertyItem(item: PropertyItem, modifier: Modifier = Modifier) {
    val colors = LocalColorScheme.current

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(52.dp)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = item.label,
            color = colors.onSurface,
            fontSize = 14.sp
        )

        item.content()
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
            .padding(start = 8.dp, top = 8.dp, end = 8.dp, bottom = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        val buttonModifier = Modifier
            .height(48.dp)
            .weight(0.5f)

        CancelButton(
            modifier = buttonModifier,
            title = stringResource(R.string.action_cancel),
            onClick = onCancelButtonClick
        )

        Spacer(modifier = Modifier.width(24.dp))

        ConfirmButton(
            modifier = buttonModifier,
            title = stringResource(R.string.action_save),
            onClick = onSaveButtonClick
        )
    }
}
