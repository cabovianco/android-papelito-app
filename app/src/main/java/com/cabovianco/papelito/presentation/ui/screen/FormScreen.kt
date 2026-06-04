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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
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
                        if (event.error is AppError.InvalidNoteParametersError) emptyNoteErrorMessage
                        else saveErrorMessage
                    )
                }
            }
        }
    }

    FormScreenContent(
        uiState = uiState,
        snackbarHostState = snackbarHostState,
        onNoteTextChange = viewModel::onNoteTextUpdate,
        onNoteBackgroundColorChange = viewModel::onNoteBackgroundColorUpdate,
        onNoteFontColorChange = viewModel::onNoteFontColorUpdate,
        onNoteFontSizeChange = viewModel::onNoteFontSizeUpdate,
        onNoteFontWeightChange = viewModel::onNoteFontWeightUpdate,
        onNoteFontFamilyChange = viewModel::onNoteFontFamilyUpdate,
        onSaveButtonClick = onSaveButtonClick,
        onCancelButtonClick = onCancelButtonClick,
        modifier = modifier
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
        containerColor = MaterialTheme.colorScheme.background,
        contentColor = Color.Transparent
    ) { paddingValues ->
        FormNoteContent(
            uiState = uiState,
            onNoteTextChange = onNoteTextChange,
            onNoteBackgroundColorChange = onNoteBackgroundColorChange,
            onNoteFontColorChange = onNoteFontColorChange,
            onNoteFontSizeChange = onNoteFontSizeChange,
            onNoteFontWeightChange = onNoteFontWeightChange,
            onNoteFontFamilyChange = onNoteFontFamilyChange,
            modifier = Modifier
                .padding(paddingValues)
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
        val buttonModifier = Modifier
            .height(58.dp)
            .weight(0.5f)

        SecondaryButton(
            modifier = buttonModifier,
            title = stringResource(R.string.action_cancel),
            onClick = onCancelButtonClick
        )

        Spacer(modifier = Modifier.width(24.dp))

        PrimaryButton(
            modifier = buttonModifier,
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
            backgroundColor = uiState.backgroundColor.color,
            fontColor = uiState.fontColor.color,
            fontSize = uiState.fontSize,
            fontWeight = uiState.fontWeight.fontWeight,
            fontFamily = uiState.fontFamily.fontFamily
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
            onTextChange = onTextChange,
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
            modifier = Modifier.fillMaxSize(),
            value = text,
            onValueChange = onTextChange,
            textStyle = textStyle,
            placeholder = { Text(text = stringResource(R.string.note_hint), style = textStyle) },
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
        ColorPropertyEditor(backgroundColor, onBackgroundColorChange, fontColor, onFontColorChange)
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
            PropertyItem(
                label = stringResource(R.string.background_color_label),
                preview = { ColorPreview(backgroundColor.color) },
                editor = {
                    ColorEditor(
                        backgroundColor,
                        onBackgroundColorChange,
                        R.drawable.background_color_selector
                    )
                }
            ),
            PropertyItem(
                label = stringResource(R.string.font_color_label),
                preview = { ColorPreview(fontColor.color) },
                editor = {
                    ColorEditor(
                        fontColor,
                        onFontColorChange,
                        R.drawable.font_color_selector
                    )
                }
            )
        ),
        modifier = modifier
    )
}

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
    @DrawableRes onSelectedIconId: Int,
    modifier: Modifier = Modifier
) {
    EditorOptionsGrid(
        options = NoteColor.entries,
        optionContent = { noteColor ->
            ColorEditorItem(
                color = noteColor.color,
                onClick = { onColorChange(noteColor) },
                isSelected = noteColor == color,
                onSelectedIconId = onSelectedIconId,
                modifier = Modifier.size(60.dp)
            )
        },
        modifier = modifier,
        columns = GridCells.FixedSize(60.dp)
    )
}

@Composable
private fun ColorEditorItem(
    color: Color,
    onClick: () -> Unit,
    isSelected: Boolean,
    @DrawableRes onSelectedIconId: Int,
    modifier: Modifier = Modifier
) {
    ElevatedButton(
        modifier = modifier,
        onClick = onClick,
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.elevatedButtonColors(
            containerColor = color,
            contentColor = if (color.luminance() > 0.5f) NoteColor.NEUTRAL_900.color else NoteColor.NEUTRAL_100.color
        ),
        contentPadding = PaddingValues(0.dp)
    ) {
        if (isSelected) {
            Icon(painter = painterResource(onSelectedIconId), contentDescription = null)
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
            PropertyItem(
                label = stringResource(R.string.font_size_label),
                preview = { FontPreview("${fontSize.toInt()}") },
                editor = { FontSizeEditor(fontSize, onFontSizeChange, Modifier.height(256.dp)) }
            ),
            PropertyItem(
                label = stringResource(R.string.font_weight_label),
                preview = { FontPreview(label = "Aa", fontWeight = fontWeight.fontWeight) },
                editor = {
                    FontWeightEditor(
                        fontWeight,
                        onFontWeightChange,
                        Modifier.height(256.dp)
                    )
                }
            ),
            PropertyItem(
                label = stringResource(R.string.font_family_label),
                preview = { FontPreview(label = "Aa", fontFamily = fontFamily.fontFamily) },
                editor = {
                    FontFamilyEditor(
                        fontFamily,
                        onFontFamilyChange,
                        Modifier.height(256.dp)
                    )
                }
            )
        ),
        modifier = modifier
    )
}

@Composable
private fun FontPreview(
    label: String,
    modifier: Modifier = Modifier,
    fontSize: Float = 16f,
    fontWeight: FontWeight = FontWeight.Medium,
    fontFamily: FontFamily = FontFamily.Default
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            fontSize = fontSize.sp,
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = fontWeight,
            fontFamily = fontFamily,
            maxLines = 1,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FontSizeEditor(
    fontSize: Float,
    onFontSizeChange: (Float) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
            .padding(horizontal = 24.dp, vertical = 32.dp),
        verticalArrangement = Arrangement.spacedBy(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        FontPreview(
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp),
            label = "Aa",
            fontSize = fontSize
        )

        Slider(
            modifier = Modifier.fillMaxWidth(),
            value = fontSize,
            onValueChange = onFontSizeChange,
            valueRange = 16f..40f,
            steps = 11,
            track = { sliderState ->
                SliderDefaults.Track(
                    sliderState = sliderState,
                    colors = SliderDefaults.colors(
                        thumbColor = MaterialTheme.colorScheme.primary,
                        activeTrackColor = MaterialTheme.colorScheme.primary,
                        activeTickColor = MaterialTheme.colorScheme.background,
                        inactiveTrackColor = MaterialTheme.colorScheme.background
                    ),
                    modifier = Modifier.height(24.dp)
                )
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FontWeightEditor(
    fontWeight: NoteFontWeight,
    onFontWeightChange: (NoteFontWeight) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
            .padding(horizontal = 24.dp, vertical = 32.dp),
        verticalArrangement = Arrangement.spacedBy(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        FontPreview(
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp),
            label = "Aa",
            fontSize = 40f,
            fontWeight = fontWeight.fontWeight
        )

        val options = NoteFontWeight.entries

        SingleChoiceSegmentedButtonRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
        ) {
            options.forEachIndexed { index, option ->
                SegmentedButton(
                    selected = option == fontWeight,
                    onClick = { onFontWeightChange(option) },
                    shape = SegmentedButtonDefaults.itemShape(
                        index = index,
                        count = options.size,
                        baseShape = RoundedCornerShape(16.dp)
                    ),
                    colors = SegmentedButtonDefaults.colors(
                        activeContainerColor = MaterialTheme.colorScheme.primary,
                        activeContentColor = MaterialTheme.colorScheme.onPrimary,
                        activeBorderColor = MaterialTheme.colorScheme.primary,
                        inactiveContainerColor = MaterialTheme.colorScheme.surface,
                        inactiveContentColor = MaterialTheme.colorScheme.onSurface,
                    ),
                    label = {
                        Text(
                            text = stringResource(
                                when (option) {
                                    NoteFontWeight.REGULAR -> R.string.font_weight_regular
                                    NoteFontWeight.BOLD -> R.string.font_weight_bold
                                }
                            ),
                            style = MaterialTheme.typography.titleSmall
                        )
                    },
                    icon = {}
                )
            }
        }
    }
}

@Composable
private fun FontFamilyEditor(
    fontFamily: NoteFontFamily,
    onFontFamilyChange: (NoteFontFamily) -> Unit,
    modifier: Modifier = Modifier
) {
    EditorOptionsGrid(
        options = NoteFontFamily.entries,
        optionContent = { item ->
            FontEditorItem(
                onClick = { onFontFamilyChange(item) },
                isSelected = item == fontFamily
            ) {
                Text(
                    text = "Aa",
                    style = MaterialTheme.typography.titleMedium,
                    fontFamily = item.fontFamily
                )
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
        items(options) { item ->
            optionContent(item)
        }
    }
}

@Composable
private fun FontEditorItem(
    onClick: () -> Unit,
    isSelected: Boolean,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    ElevatedButton(
        modifier = modifier.size(64.dp),
        onClick = onClick,
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.elevatedButtonColors(
            containerColor = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary,
            contentColor = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSecondary
        ),
        elevation = if (isSelected) ButtonDefaults.elevatedButtonElevation(
            pressedElevation = 0.dp,
            defaultElevation = 0.dp,
            hoveredElevation = 0.dp,
            focusedElevation = 0.dp
        ) else ButtonDefaults.elevatedButtonElevation(),
        contentPadding = PaddingValues(0.dp)
    ) {
        content()
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
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = title,
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        items.forEachIndexed { index, item ->
            val shape = when {
                items.size == 1 -> RoundedCornerShape(16.dp)
                index == 0 -> RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
                index == items.lastIndex -> RoundedCornerShape(
                    bottomStart = 16.dp,
                    bottomEnd = 16.dp
                )

                else -> RoundedCornerShape(0.dp)
            }

            NotePropertyItem(item = item, shape = shape)

            if (index != items.lastIndex) {
                HorizontalDivider(
                    modifier = Modifier.fillMaxWidth(),
                    color = MaterialTheme.colorScheme.background
                )
            }
        }
    }
}

@Composable
private fun NotePropertyItem(item: PropertyItem, shape: Shape, modifier: Modifier = Modifier) {
    var clicked by remember { mutableStateOf(false) }

    ElevatedCard(
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp),
        onClick = { clicked = true },
        shape = shape,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface
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
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.bodyLarge
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
    ModalBottomSheet(
        modifier = modifier
            .systemBarsPadding()
            .padding(16.dp),
        onDismissRequest = onDismissRequest,
        shape = RoundedCornerShape(24.dp),
        containerColor = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.onSurface,
        dragHandle = null,
        contentWindowInsets = { WindowInsets(bottom = 0.dp) }
    ) {
        content()
    }
}
