package com.cabovianco.papelito.domain.model.note

import androidx.compose.ui.text.font.FontFamily
import com.cabovianco.papelito.presentation.ui.theme.Fredoka
import com.cabovianco.papelito.presentation.ui.theme.Handlee
import com.cabovianco.papelito.presentation.ui.theme.IndieFlower
import com.cabovianco.papelito.presentation.ui.theme.JetBrainsMono
import com.cabovianco.papelito.presentation.ui.theme.Lora
import com.cabovianco.papelito.presentation.ui.theme.Satisfy

enum class NoteFontFamily(val fontFamily: FontFamily) {
    JETBRAINS_MONO(JetBrainsMono),
    FREDOKA(Fredoka),
    LORA(Lora),
    SATISFY(Satisfy),
    HANDLEE(Handlee),
    INDIE_FLOWER(IndieFlower)
}
