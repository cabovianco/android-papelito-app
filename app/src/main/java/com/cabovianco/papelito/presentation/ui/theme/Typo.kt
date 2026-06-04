package com.cabovianco.papelito.presentation.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.sp
import com.cabovianco.papelito.R

val Fascinate = FontFamily(
    Font(R.font.fascinate_regular)
)

val Fredoka = FontFamily(
    Font(R.font.fredoka_regular)
)

val Handlee = FontFamily(
    Font(R.font.handlee_regular)
)

val IndieFlower = FontFamily(
    Font(R.font.indie_flower_regular)
)

val JetBrainsMono = FontFamily(
    Font(R.font.jetbrains_mono_regular)
)

val Lora = FontFamily(
    Font(R.font.lora_regular)
)

val Satisfy = FontFamily(
    Font(R.font.satisfy_regular)
)

val Typography = Typography(
    bodyMedium = TextStyle(
        fontFamily = Fredoka,
        fontSize = 14.sp
    ),
    bodyLarge = TextStyle(
        fontFamily = Fredoka,
        fontSize = 16.sp
    ),
    titleSmall = TextStyle(
        fontFamily = Fredoka,
        fontSize = 18.sp
    ),
    titleMedium = TextStyle(
        fontFamily = Fredoka,
        fontSize = 20.sp
    ),
    titleLarge = TextStyle(
        fontFamily = Fredoka,
        fontSize = 22.sp
    ),
    headlineSmall = TextStyle(
        fontFamily = Fredoka,
        fontSize = 24.sp
    ),
    headlineLarge = TextStyle(
        fontFamily = Fascinate,
        fontSize = 32.sp
    )
)
