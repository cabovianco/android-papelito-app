package com.cabovianco.papelito.presentation.ui.screen

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cabovianco.papelito.R
import com.cabovianco.papelito.domain.model.AppError
import com.cabovianco.papelito.domain.model.note.NoteColor
import com.cabovianco.papelito.domain.model.note.NoteFontFamily
import com.cabovianco.papelito.domain.model.note.NoteFontWeight
import com.cabovianco.papelito.presentation.event.FormNoteUiEvent
import com.cabovianco.papelito.presentation.state.FormNoteUiState
import com.cabovianco.papelito.presentation.ui.screen.shared.PrimaryButton
import com.cabovianco.papelito.presentation.ui.screen.shared.SecondaryButton
import com.cabovianco.papelito.presentation.ui.theme.LocalColorScheme
import com.cabovianco.papelito.presentation.viewmodel.FormNoteViewModel

@Composable
fun FormScreen(
    viewModel: FormNoteViewModel,
    onSaveButtonClick: () -> Unit,
    onCancelButtonClick: () -> Unit,
    onSaveEvent: () -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    val emptyNoteErrorMessage = stringResource(R.string.empty_note_error_message)
    val saveErrorMessage = stringResource(R.string.save_error_message)

    LaunchedEffect(Unit) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is FormNoteUiEvent.Saved -> onSaveEvent()
                is FormNoteUiEvent.Error -> {
                    snackbarHostState.showSnackbar(
                        when (event.error) {
                            is AppError.InvalidNoteParametersError -> emptyNoteErrorMessage
                            else -> saveErrorMessage
                        }
                    )
                }
            }
        }
    }

    FormScreenContent(
        uiState,
        snackbarHostState,
        onNoteTextChange = { viewModel.onNoteTextUpdate(it) },
        onNoteBackgroundColorChange = { viewModel.onNoteBackgroundColorUpdate(it) },
        onNoteFontColorChange = { viewModel.onNoteFontColorUpdate(it) },
        onNoteFontSizeChange = { viewModel.onNoteFontSizeUpdate(it) },
        onNoteFontWeightChange = { viewModel.onNoteFontWeightUpdate(it) },
        onNoteFontFamilyChange = { viewModel.onNoteFontFamilyUpdate(it) },
        onSaveButtonClick,
        onCancelButtonClick,
        modifier
    )
}

@Composable
fun FormScreenContent(
    uiState: FormNoteUiState,
    snackbarHostState: SnackbarHostState,
    onNoteTextChange: (String) -> Unit,
    onNoteBackgroundColorChange: (NoteColor) -> Unit,
    onNoteFontColorChange: (NoteColor) -> Unit,
    onNoteFontSizeChange: (Float) -> Unit,
    onNoteFontWeightChange: (NoteFontWeight) -> Unit,
    onNoteFontFamilyChange: (NoteFontFamily) -> Unit,
    onSaveButtonClick: () -> Unit,
    onCancelButtonClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = LocalColorScheme.current

    Scaffold(
        modifier = modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(snackbarHostState) },
        floatingActionButton = {
            FormActionButtons(
                onSaveButtonClick = onSaveButtonClick,
                onCancelButtonClick = onCancelButtonClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            )
        },
        floatingActionButtonPosition = FabPosition.Center,
        containerColor = colors.background,
        contentColor = Color.Transparent
    ) {
        FormNoteContent(
            uiState,
            onNoteTextChange,
            onNoteBackgroundColorChange,
            onNoteFontColorChange,
            onNoteFontSizeChange,
            onNoteFontWeightChange,
            onNoteFontFamilyChange,
            modifier = Modifier
                .padding(it)
                .padding(horizontal = 16.dp)
        )
    }
}

@Composable
private fun FormActionButtons(
    onSaveButtonClick: () -> Unit,
    onCancelButtonClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        val modifier = Modifier
            .height(58.dp)
            .weight(0.5f)

        SecondaryButton(
            modifier = modifier,
            title = stringResource(R.string.action_cancel),
            onClick = onCancelButtonClick
        )

        Spacer(modifier = Modifier.width(24.dp))

        PrimaryButton(
            modifier = modifier,
            title = stringResource(R.string.action_save),
            onClick = onSaveButtonClick
        )
    }
}

@Composable
private fun FormNoteContent(
    uiState: FormNoteUiState,
    onNoteTextChange: (String) -> Unit,
    onNoteBackgroundColorChange: (NoteColor) -> Unit,
    onNoteFontColorChange: (NoteColor) -> Unit,
    onNoteFontSizeChange: (Float) -> Unit,
    onNoteFontWeightChange: (NoteFontWeight) -> Unit,
    onNoteFontFamilyChange: (NoteFontFamily) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        NotePreviewSection(
            text = uiState.text,
            onTextChange = onNoteTextChange,
            backgroundColor = uiState.backgroundColor.value,
            fontColor = uiState.fontColor.value,
            fontSize = uiState.fontSize,
            fontWeight = uiState.fontWeight.value,
            fontFamily = uiState.fontFamily.value
        )

        NotePropertiesEditor(
            backgroundColor = uiState.backgroundColor,
            onBackgroundColorChange = onNoteBackgroundColorChange,
            fontColor = uiState.fontColor,
            onFontColorChange = onNoteFontColorChange,
            fontSize = uiState.fontSize,
            onFontSizeChange = onNoteFontSizeChange,
            fontWeight = uiState.fontWeight,
            onFontWeightChange = onNoteFontWeightChange,
            fontFamily = uiState.fontFamily,
            onFontFamilyChange = onNoteFontFamilyChange,
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .weight(1f)
        )
    }
}

@Composable
private fun NotePreviewSection(
    text: String,
    onTextChange: (String) -> Unit,
    backgroundColor: Color,
    fontColor: Color,
    fontSize: Float,
    fontWeight: FontWeight,
    fontFamily: FontFamily,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight(0.45f),
        contentAlignment = Alignment.Center
    ) {
        EditableNoteCard(
            text = text,
            onTextChange = { onTextChange(it) },
            backgroundColor = backgroundColor,
            fontColor = fontColor,
            fontSize = fontSize,
            fontWeight = fontWeight,
            fontFamily = fontFamily
        )
    }
}

@Composable
private fun EditableNoteCard(
    text: String,
    onTextChange: (String) -> Unit,
    backgroundColor: Color,
    fontColor: Color,
    fontSize: Float,
    fontWeight: FontWeight,
    fontFamily: FontFamily,
    modifier: Modifier = Modifier
) {
    val textStyle = TextStyle(
        color = fontColor,
        fontSize = fontSize.sp,
        fontWeight = fontWeight,
        fontFamily = fontFamily
    )

    ElevatedCard(
        modifier = modifier.size(288.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.elevatedCardColors(containerColor = backgroundColor)
    ) {
        TextField(
            modifier = Modifier
                .fillMaxSize(),
            value = text,
            onValueChange = onTextChange,
            textStyle = textStyle,
            placeholder = {
                Text(
                    text = stringResource(R.string.note_hint),
                    style = textStyle
                )
            },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                disabledContainerColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                cursorColor = fontColor
            )
        )
    }
}

@Composable
private fun NotePropertiesEditor(
    backgroundColor: NoteColor,
    onBackgroundColorChange: (NoteColor) -> Unit,
    fontColor: NoteColor,
    onFontColorChange: (NoteColor) -> Unit,
    fontSize: Float,
    onFontSizeChange: (Float) -> Unit,
    fontWeight: NoteFontWeight,
    onFontWeightChange: (NoteFontWeight) -> Unit,
    fontFamily: NoteFontFamily,
    onFontFamilyChange: (NoteFontFamily) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        ColorPropertyEditor(
            backgroundColor,
            onBackgroundColorChange,
            fontColor,
            onFontColorChange
        )

        FontPropertyEditor(
            fontSize,
            onFontSizeChange,
            fontWeight,
            onFontWeightChange,
            fontFamily,
            onFontFamilyChange
        )

        Spacer(
            modifier = Modifier
                .systemBarsPadding()
                .padding(vertical = 8.dp)
        )
    }
}

@Composable
private fun ColorPropertyEditor(
    backgroundColor: NoteColor,
    onBackgroundColorChange: (NoteColor) -> Unit,
    fontColor: NoteColor,
    onFontColorChange: (NoteColor) -> Unit,
    modifier: Modifier = Modifier
) {
    NotePropertyMenu(
        title = stringResource(R.string.color_selection_title),
        items = listOf(
            backgroundColorPropertyItem(
                color = backgroundColor,
                onColorChange = onBackgroundColorChange
            ),
            fontColorPropertyItem(
                color = fontColor,
                onColorChange = onFontColorChange
            )
        ),
        modifier
    )
}

@Composable
private fun backgroundColorPropertyItem(color: NoteColor, onColorChange: (NoteColor) -> Unit) =
    PropertyItem(
        label = stringResource(R.string.background_color_label),
        preview = { ColorPreview(color.value) },
        editor = { ColorEditor(color, onColorChange) }
    )

@Composable
private fun fontColorPropertyItem(color: NoteColor, onColorChange: (NoteColor) -> Unit) =
    PropertyItem(
        label = stringResource(R.string.font_color_label),
        preview = { ColorPreview(color.value) },
        editor = { ColorEditor(color, onColorChange) }
    )

@Composable
private fun ColorPreview(color: Color, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .size(32.dp)
            .background(color, RoundedCornerShape(10.dp))
    )
}

@Composable
private fun ColorEditor(
    color: NoteColor,
    onColorChange: (NoteColor) -> Unit,
    modifier: Modifier = Modifier
) {
    EditorOptionsGrid(
        options = NoteColor.entries,
        optionContent = {
            ColorEditorItem(
                color = it.value,
                onClick = { onColorChange(it) },
                isSelected = it == color,
                modifier = Modifier.size(50.dp)
            )
        },
        modifier,
        columns = GridCells.FixedSize(50.dp)
    )
}

@Composable
private fun ColorEditorItem(
    color: Color,
    onClick: () -> Unit,
    isSelected: Boolean,
    modifier: Modifier = Modifier
) {
    ElevatedButton(
        modifier = modifier,
        onClick = onClick,
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.elevatedButtonColors(
            containerColor = color,
            contentColor = if (color.luminance() > 0.5f) Color.Black else Color.White
        ),
        contentPadding = PaddingValues(0.dp)
    ) {
        if (isSelected) {
            Icon(
                painter = painterResource(R.drawable.color_selector),
                contentDescription = null
            )
        }
    }
}

@Composable
private fun FontPropertyEditor(
    fontSize: Float,
    onFontSizeChange: (Float) -> Unit,
    fontWeight: NoteFontWeight,
    onFontWeightChange: (NoteFontWeight) -> Unit,
    fontFamily: NoteFontFamily,
    onFontFamilyChange: (NoteFontFamily) -> Unit,
    modifier: Modifier = Modifier
) {
    NotePropertyMenu(
        title = stringResource(R.string.font_selection_title),
        items = listOf(
            fontSizePropertyItem(fontSize, onFontSizeChange),
            fontWeightPropertyItem(fontWeight, onFontWeightChange),
            fontFamilyPropertyItem(fontFamily, onFontFamilyChange)
        ),
        modifier
    )
}

@Composable
private fun fontSizePropertyItem(fontSize: Float, onFontSizeChange: (Float) -> Unit) =
    PropertyItem(
        label = stringResource(R.string.font_size_label),
        preview = { FontPreview("${fontSize.toInt()}") },
        editor = {
            FontSizeEditor(
                fontSize,
                onFontSizeChange,
                modifier = Modifier.height(256.dp)
            )
        }
    )

@Composable
private fun FontPreview(
    label: String,
    modifier: Modifier = Modifier,
    fontWeight: FontWeight = FontWeight.Medium,
    fontFamily: FontFamily? = null
) {
    Box(modifier) {
        Text(text = label, fontSize = 16.sp, fontWeight = fontWeight, fontFamily = fontFamily)
    }
}

@Composable
private fun FontSizeEditor(
    fontSize: Float,
    onFontSizeChange: (Float) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(32.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        FontSizeEditorButton(
            iconId = R.drawable.decrease_font_size_button,
            onClick = { onFontSizeChange(fontSize - 1f) }
        )

        Text(text = "${fontSize.toInt()}", fontSize = 72.sp, fontWeight = FontWeight.Bold)

        FontSizeEditorButton(
            iconId = R.drawable.increase_font_size_button,
            onClick = { onFontSizeChange(fontSize + 1f) }
        )
    }
}

@Composable
private fun FontSizeEditorButton(
    @DrawableRes iconId: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = LocalColorScheme.current

    ElevatedButton(
        modifier = modifier,
        onClick = onClick,
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.elevatedButtonColors(
            containerColor = colors.secondary,
            contentColor = colors.onSecondary
        )
    ) {
        Icon(painter = painterResource(iconId), contentDescription = null)
    }
}

@Composable
private fun fontWeightPropertyItem(
    fontWeight: NoteFontWeight,
    onFontWeightChange: (NoteFontWeight) -> Unit
) =
    PropertyItem(
        label = stringResource(R.string.font_weight_label),
        preview = {
            FontPreview(label = "Aa", fontWeight = fontWeight.value)
        },
        editor = {
            FontWeightEditor(
                fontWeight,
                onFontWeightChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(256.dp)
            )
        }
    )

@Composable
private fun FontWeightEditor(
    fontWeight: NoteFontWeight,
    onFontWeightChange: (NoteFontWeight) -> Unit,
    modifier: Modifier = Modifier
) {
    EditorOptionsGrid(
        options = NoteFontWeight.entries,
        optionContent = {
            FontEditorItem(
                onClick = { onFontWeightChange(it) },
                isSelected = it == fontWeight
            ) {
                Text(text = "Aa", fontSize = 20.sp, fontWeight = it.value)
            }
        },
        modifier = modifier
    )
}

@Composable
private fun FontEditorItem(
    onClick: () -> Unit,
    isSelected: Boolean,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    val colors = LocalColorScheme.current

    ElevatedButton(
        modifier = modifier.size(64.dp),
        onClick = onClick,
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.elevatedButtonColors(
            containerColor = if (isSelected) colors.primary else colors.secondary,
            contentColor = if (isSelected) colors.onPrimary else colors.onSecondary
        ),
        contentPadding = PaddingValues(0.dp)
    ) {
        content()
    }
}

@Composable
private fun fontFamilyPropertyItem(
    fontFamily: NoteFontFamily,
    onFontFamilyChange: (NoteFontFamily) -> Unit
) =
    PropertyItem(
        label = stringResource(R.string.font_family_label),
        preview = {
            FontPreview(
                label = "Aa",
                fontFamily = fontFamily.value
            )
        },
        editor = {
            FontFamilyEditor(
                fontFamily,
                onFontFamilyChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(256.dp)
            )
        }
    )

@Composable
private fun FontFamilyEditor(
    fontFamily: NoteFontFamily,
    onFontFamilyChange: (NoteFontFamily) -> Unit,
    modifier: Modifier = Modifier
) {
    EditorOptionsGrid(
        options = NoteFontFamily.entries,
        optionContent = {
            FontEditorItem(
                onClick = { onFontFamilyChange(it) },
                isSelected = it == fontFamily
            ) {
                Text(text = "Aa", fontSize = 20.sp, fontFamily = it.value)
            }
        },
        modifier = modifier
    )
}

@Composable
private fun <T> EditorOptionsGrid(
    options: List<T>,
    optionContent: @Composable (T) -> Unit,
    modifier: Modifier = Modifier,
    columns: GridCells = GridCells.FixedSize(64.dp),
) {
    LazyVerticalGrid(
        modifier = modifier,
        columns = columns,
        verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
        horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterHorizontally),
        contentPadding = PaddingValues(16.dp)
    ) {
        items(options) {
            optionContent(it)
        }
    }
}

private data class PropertyItem(
    val label: String,
    val preview: @Composable () -> Unit,
    val editor: @Composable () -> Unit
)

@Composable
private fun NotePropertyMenu(
    title: String,
    items: List<PropertyItem>,
    modifier: Modifier = Modifier
) {
    val colors = LocalColorScheme.current

    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = title,
            color = colors.onSurface,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        items.forEachIndexed { index, item ->
            NotePropertyItem(
                item, shape = when (index) {
                    0 -> RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
                    items.lastIndex -> RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp)
                    else -> RoundedCornerShape(0.dp)
                }
            )

            if (index != items.lastIndex) {
                HorizontalDivider(modifier = Modifier.fillMaxWidth(), color = colors.background)
            }
        }
    }
}

@Composable
private fun NotePropertyItem(item: PropertyItem, shape: Shape, modifier: Modifier = Modifier) {
    val colors = LocalColorScheme.current
    var clicked by remember { mutableStateOf(false) }

    ElevatedCard(
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp),
        onClick = { clicked = true },
        shape = shape,
        colors = CardDefaults.cardColors(
            containerColor = colors.surface,
            contentColor = colors.onSurface
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = item.label,
                color = colors.onSurface,
                fontSize = 15.sp
            )

            item.preview()
        }
    }

    if (clicked) {
        NotePropertyItemEditorBottomSheet(
            onDismissRequest = { clicked = false },
            content = item.editor
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun NotePropertyItemEditorBottomSheet(
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
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
        content()
    }
}
