package com.cabovianco.papelito.presentation.ui.screen.shared

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cabovianco.papelito.R
import com.cabovianco.papelito.presentation.ui.theme.LocalColorScheme

@Composable
fun FormScreen(
    onNavigateBackButtonClick: () -> Unit,
    noteTextValue: String,
    onNoteTextChange: (String) -> Unit,
    onCancelButtonClick: () -> Unit,
    onSaveButtonClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        NavigateBackButton { onNavigateBackButtonClick() }
        NoteContainer(noteTextValue, onNoteTextChange)
        NoteEditorContainer(onCancelButtonClick, onSaveButtonClick, modifier = Modifier.weight(1f))
    }
}

@Composable
private fun NavigateBackButton(modifier: Modifier = Modifier, onClick: () -> Unit) {
    val colors = LocalColorScheme.current

    FilledIconButton(
        modifier = modifier,
        onClick = onClick,
        colors = IconButtonDefaults.iconButtonColors(
            containerColor = colors.primary,
            contentColor = colors.onPrimary
        )
    ) {
        Icon(
            painter = painterResource(R.drawable.navigate_back),
            contentDescription = null,
            tint = colors.onPrimary
        )
    }
}

@Composable
private fun NoteContainer(
    noteTextValue: String,
    onNoteTextChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight(0.5f),
        contentAlignment = Alignment.Center
    ) {
        NoteContent(value = noteTextValue, onValueChange = { onNoteTextChange(it) })
    }
}

@Composable
private fun NoteContent(
    value: String,
    modifier: Modifier = Modifier,
    onValueChange: (String) -> Unit
) {
    ElevatedCard(
        modifier = modifier.fillMaxSize(0.8f),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White, contentColor = Color.Black),
        elevation = CardDefaults.elevatedCardElevation(2.dp)
    ) {
        BasicTextField(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            value = value,
            onValueChange = { onValueChange(it) }
        )
    }
}

@Composable
private fun NoteEditorContainer(
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
        NoteEditorContent(modifier = Modifier.weight(1f))
        NoteEditorActions(onCancelButtonClick, onSaveButtonClick)
    }
}

@Composable
private fun NoteEditorContent(modifier: Modifier = Modifier) {
    Box(modifier) {}
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
        NoteEditorCancelButton(modifier = Modifier.weight(0.5f)) { onCancelButtonClick() }
        Spacer(modifier = Modifier.width(16.dp))
        NoteEditorSaveButton(modifier = Modifier.weight(0.5f)) { onSaveButtonClick() }
    }
}

@Composable
private fun NoteEditorCancelButton(modifier: Modifier = Modifier, onClick: () -> Unit) {
    SecondaryButton(
        modifier = modifier.height(48.dp),
        title = stringResource(R.string.cancel_button),
        onClick = onClick
    )
}

@Composable
private fun NoteEditorSaveButton(modifier: Modifier = Modifier, onClick: () -> Unit) {
    PrimaryButton(
        modifier = modifier.height(48.dp),
        title = stringResource(R.string.save_button),
        onClick = onClick
    )
}

@Composable
private fun NotePropertyContainer(
    title: String,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    val colors = LocalColorScheme.current

    Column(modifier = modifier) {
        Text(
            text = title,
            color = colors.onSurface,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )

        content()
    }
}
