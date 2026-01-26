package com.cabovianco.papelito.domain.model

import androidx.compose.ui.text.font.FontFamily

enum class NoteFontFamily(val value: FontFamily) {
    SERIF(FontFamily.Serif),
    SANS_SERIF(FontFamily.SansSerif),
    MONOSPACE(FontFamily.Monospace),
    CURSIVE(FontFamily.Cursive)
}
