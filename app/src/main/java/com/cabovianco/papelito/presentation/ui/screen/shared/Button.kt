package com.cabovianco.papelito.presentation.ui.screen.shared

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cabovianco.papelito.presentation.ui.theme.LocalColorScheme

@Composable
fun PrimaryButton(title: String, onClick: () -> Unit, modifier: Modifier = Modifier) {
    val colors = LocalColorScheme.current

    Button(
        modifier = modifier.fillMaxWidth(),
        onClick = onClick,
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = colors.primary,
            contentColor = colors.onPrimary
        )
    ) {
        Text(text = title, color = colors.onPrimary, fontSize = 18.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun SecondaryButton(title: String, onClick: () -> Unit, modifier: Modifier = Modifier) {
    val colors = LocalColorScheme.current

    Button(
        modifier = modifier.fillMaxWidth(),
        onClick = onClick,
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = colors.secondary,
            contentColor = colors.onSecondary
        )
    ) {
        Text(
            text = title,
            color = colors.onSecondary,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )
    }
}
