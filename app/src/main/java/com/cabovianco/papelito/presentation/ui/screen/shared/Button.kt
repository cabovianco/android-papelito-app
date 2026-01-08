package com.cabovianco.papelito.presentation.ui.screen.shared

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cabovianco.papelito.presentation.ui.theme.LocalColorScheme

@Composable
fun ConfirmButton(title: String, onClick: () -> Unit, modifier: Modifier = Modifier) {
    val colors = LocalColorScheme.current

    Button(
        title,
        containerColor = colors.primary,
        contentColor = colors.onPrimary,
        onClick,
        modifier
    )
}

@Composable
fun CancelButton(title: String, onClick: () -> Unit, modifier: Modifier = Modifier) {
    val colors = LocalColorScheme.current

    Button(
        title,
        containerColor = colors.secondary,
        contentColor = colors.onSecondary,
        onClick,
        modifier
    )
}

@Composable
private fun Button(
    title: String,
    containerColor: Color,
    contentColor: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    ElevatedButton(
        modifier = modifier.fillMaxWidth(),
        onClick = onClick,
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.elevatedButtonColors(
            containerColor = containerColor,
            contentColor = contentColor
        )
    ) {
        Text(
            text = title,
            color = contentColor,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )
    }
}
