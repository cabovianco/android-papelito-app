package com.cabovianco.papelito.presentation.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

data class ColorScheme(
    val background: Color,
    val onBackground: Color,
    val surface: Color,
    val onSurface: Color,
    val primary: Color,
    val onPrimary: Color,
    val secondary: Color,
    val onSecondary: Color,
    val accent: Color,
    val onAccent: Color
)

private val lightColorScheme = ColorScheme(
    background = lightBackground,
    onBackground = lightOnBackground,
    surface = lightSurface,
    onSurface = lightOnSurface,
    primary = lightPrimary,
    onPrimary = lightOnPrimary,
    secondary = lightSecondary,
    onSecondary = lightOnSecondary,
    accent = lightAccent,
    onAccent = lightOnAccent
)

val LocalColorScheme = staticCompositionLocalOf { lightColorScheme }

@Composable
fun PapelitoTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) lightColorScheme else lightColorScheme
    CompositionLocalProvider(
        value = LocalColorScheme provides colorScheme,
        content = content
    )
}