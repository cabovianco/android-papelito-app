package com.cabovianco.papelito.presentation.ui.screen.shared

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
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
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cabovianco.papelito.R
import com.cabovianco.papelito.domain.model.NoteColor
import com.cabovianco.papelito.domain.model.NoteFontFamily
import com.cabovianco.papelito.domain.model.NoteFontWeight
import com.cabovianco.papelito.presentation.state.FormNoteUiState
import com.cabovianco.papelito.presentation.ui.theme.LocalColorScheme

@Composable
fun FormScreen(
    uiState: FormNoteUiState,
    onNoteTextChange: (String) -> Unit,
    onNoteBackgroundColorChange: (NoteColor) -> Unit,
    onNoteFontColorChange: (NoteColor) -> Unit,
    onNoteFontSizeChange: (Float) -> Unit,
    onNoteFontWeightChange: (NoteFontWeight) -> Unit,
    onNoteFontFamilyChange: (NoteFontFamily) -> Unit,
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
            onNoteFontSizeChange,
            onNoteFontWeightChange,
            onNoteFontFamilyChange,
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
            backgroundColor = uiState.noteBackgroundColor.value,
            fontColor = uiState.noteFontColor.value,
            fontSize = uiState.noteFontSize,
            fontWeight = uiState.noteFontWeight.value,
            fontFamily = uiState.noteFontFamily.value
        )
    }
}

@Composable
private fun NoteContent(
    text: String,
    onTextChange: (String) -> Unit,
    backgroundColor: Color,
    fontColor: Color,
    fontSize: Float,
    fontWeight: FontWeight,
    fontFamily: FontFamily,
    modifier: Modifier = Modifier
) {
    ElevatedCard(
        modifier = modifier.size(288.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.elevatedCardColors(containerColor = backgroundColor)
    ) {
        BasicTextField(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            value = text,
            onValueChange = { onTextChange(it) },
            textStyle = TextStyle(
                color = fontColor,
                fontSize = fontSize.sp,
                fontWeight = fontWeight,
                fontFamily = fontFamily
            ),
            cursorBrush = SolidColor(fontColor)
        )
    }
}

@Composable
private fun NoteEditorContainer(
    uiState: FormNoteUiState,
    onNoteBackgroundColorChange: (NoteColor) -> Unit,
    onNoteFontColorChange: (NoteColor) -> Unit,
    onNoteFontSizeChange: (Float) -> Unit,
    onNoteFontWeightChange: (NoteFontWeight) -> Unit,
    onNoteFontFamilyChange: (NoteFontFamily) -> Unit,
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
            onNoteFontSizeChange,
            onNoteFontWeightChange,
            onNoteFontFamilyChange,
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
        )

        NoteEditorActions(onCancelButtonClick, onSaveButtonClick)
    }
}

@Composable
private fun NoteEditorContent(
    uiState: FormNoteUiState,
    onNoteBackgroundColorChange: (NoteColor) -> Unit,
    onNoteFontColorChange: (NoteColor) -> Unit,
    onNoteFontSizeChange: (Float) -> Unit,
    onNoteFontWeightChange: (NoteFontWeight) -> Unit,
    onNoteFontFamilyChange: (NoteFontFamily) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        ColorPropertyContainer(
            uiState,
            onNoteBackgroundColorChange,
            onNoteFontColorChange
        )

        FontPropertyContainer(
            uiState,
            onNoteFontSizeChange,
            onNoteFontWeightChange,
            onNoteFontFamilyChange
        )
    }
}

@Composable
private fun ColorPropertyContainer(
    uiState: FormNoteUiState,
    onNoteBackgroundColorChange: (NoteColor) -> Unit,
    onNoteFontColorChange: (NoteColor) -> Unit,
    modifier: Modifier = Modifier
) {
    NotePropertyMenu(
        title = stringResource(R.string.color_selection_title),
        items = listOf(
            backgroundColorPropertyItem(
                color = uiState.noteBackgroundColor,
                onColorChange = onNoteBackgroundColorChange
            ),
            fontColorPropertyItem(
                color = uiState.noteFontColor,
                onColorChange = onNoteFontColorChange
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
            .background(color, CircleShape)
    )
}

@Composable
private fun ColorEditor(
    color: NoteColor,
    onColorChange: (NoteColor) -> Unit,
    modifier: Modifier = Modifier
) {
    VerticalGridEditorContainer(
        items = NoteColor.entries,
        itemContent = {
            ColorEditorItem(
                color = it.value,
                onClick = { onColorChange(it) },
                isSelected = it == color
            )
        },
        modifier
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
        modifier = modifier.size(64.dp),
        onClick = onClick,
        shape = CircleShape,
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
private fun FontPropertyContainer(
    uiState: FormNoteUiState,
    onNoteFontSizeChange: (Float) -> Unit,
    onNoteFontWeightChange: (NoteFontWeight) -> Unit,
    onNoteFontFamilyChange: (NoteFontFamily) -> Unit,
    modifier: Modifier = Modifier
) {
    NotePropertyMenu(
        title = stringResource(R.string.font_selection_title),
        items = listOf(
            fontSizePropertyItem(uiState.noteFontSize, onNoteFontSizeChange),
            fontWeightPropertyItem(uiState.noteFontWeight, onNoteFontWeightChange),
            fontFamilyPropertyItem(uiState.noteFontFamily, onNoteFontFamilyChange)
        ),
        modifier
    )
}

@Composable
private fun fontSizePropertyItem(fontSize: Float, onNoteFontSizeChange: (Float) -> Unit) =
    PropertyItem(
        label = stringResource(R.string.font_size_label),
        preview = { FontPreview("${fontSize.toInt()}") },
        editor = {
            FontSizeEditor(
                fontSize,
                onNoteFontSizeChange,
                modifier = Modifier.height(256.dp)
            )
        }
    )

@Composable
private fun FontPreview(label: String, modifier: Modifier = Modifier) {
    Box(modifier) {
        Text(text = label, fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
    }
}

@Composable
private fun FontSizeEditor(
    fontSize: Float,
    onNoteFontSizeChange: (Float) -> Unit,
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
            onClick = { onNoteFontSizeChange(fontSize - 1f) }
        )

        Text(text = "${fontSize.toInt()}", fontSize = 72.sp, fontWeight = FontWeight.Bold)

        FontSizeEditorButton(
            iconId = R.drawable.increase_font_size_button,
            onClick = { onNoteFontSizeChange(fontSize + 1f) }
        )
    }
}

@Composable
private fun FontSizeEditorButton(@DrawableRes iconId: Int, onClick: () -> Unit, modifier: Modifier = Modifier) {
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
private fun fontWeightPropertyItem(fontWeight: NoteFontWeight, onNoteFontWeightChange: (NoteFontWeight) -> Unit) =
    PropertyItem(
        label = stringResource(R.string.font_weight_label),
        preview = {
            FontPreview(
                stringResource(
                    when (fontWeight) {
                        NoteFontWeight.THIN -> R.string.thin_font_weight_name
                        NoteFontWeight.EXTRA_LIGHT -> R.string.extra_light_font_weight_name
                        NoteFontWeight.LIGHT -> R.string.light_font_weight_name
                        NoteFontWeight.NORMAL -> R.string.normal_font_weight_name
                        NoteFontWeight.MEDIUM -> R.string.medium_font_weight_name
                        NoteFontWeight.SEMI_BOLD -> R.string.semi_bold_font_weight_name
                        NoteFontWeight.BOLD -> R.string.bold_font_weight_name
                    }
                )
            )
        },
        editor = {
            FontWeightEditor(
                fontWeight,
                onNoteFontWeightChange,
                modifier = Modifier.height(256.dp)
            )
        }
    )

@Composable
private fun FontWeightEditor(
    fontWeight: NoteFontWeight,
    onNoteFontWeightChange: (NoteFontWeight) -> Unit,
    modifier: Modifier = Modifier
) {
    VerticalGridEditorContainer(
        items = NoteFontWeight.entries,
        itemContent = {
            FontEditorItem(
                onClick = { onNoteFontWeightChange(it) },
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
private fun fontFamilyPropertyItem(fontFamily: NoteFontFamily, onNoteFontFamilyChange: (NoteFontFamily) -> Unit) =
    PropertyItem(
        label = stringResource(R.string.font_family_label),
        preview = {
            FontPreview(
            stringResource(
                    when (fontFamily) {
                        NoteFontFamily.SERIF -> R.string.serif_font_family_name
                        NoteFontFamily.SANS_SERIF -> R.string.sans_serif_font_family_name
                        NoteFontFamily.MONOSPACE -> R.string.monospace_font_family_name
                        NoteFontFamily.CURSIVE -> R.string.cursive_font_family_name
                    }
                )
            )
        },
        editor = {
            FontFamilyEditor(
                fontFamily,
                onNoteFontFamilyChange,
                modifier = Modifier.height(256.dp)
            )
        }
    )

@Composable
private fun FontFamilyEditor(
    fontFamily: NoteFontFamily,
    onNoteFontFamilyChange: (NoteFontFamily) -> Unit,
    modifier: Modifier = Modifier
) {
    VerticalGridEditorContainer(
        items = NoteFontFamily.entries,
        itemContent = {
            FontEditorItem(
                onClick = { onNoteFontFamilyChange(it) },
                isSelected = it == fontFamily
            ) {
                Text(text = "Aa", fontSize = 20.sp, fontFamily = it.value)
            }
        },
        modifier = modifier
    )
}

@Composable
private fun <T> VerticalGridEditorContainer(
    items: List<T>,
    itemContent: @Composable (T) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyVerticalGrid(
        modifier = modifier.fillMaxWidth(),
        columns = GridCells.FixedSize(64.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
        horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterHorizontally),
        contentPadding = PaddingValues(16.dp)
    ) {
        items(items) {
            itemContent(it)
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
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        items.forEachIndexed { index, item ->
            NotePropertyItem(item, shape = when (index) {
                0 -> RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
                items.lastIndex -> RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp)
                else -> RoundedCornerShape(0.dp)
            })

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
                fontSize = 14.sp
            )

            item.preview()
        }
    }

    if (clicked) {
        NotePropertyItemEditorBottomSheet(onDismissRequest = { clicked = false}, content = item.editor)
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

@Composable
private fun NoteEditorActions(
    onCancelButtonClick: () -> Unit,
    onSaveButtonClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        val buttonModifier = Modifier
            .height(52.dp)
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
