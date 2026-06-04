package com.cabovianco.papelito.domain.model.note

import androidx.compose.ui.graphics.Color

enum class NoteColor(val color: Color) {
    NEUTRAL_100(Color(0xFFF4F3F1)),
    NEUTRAL_200(Color(0xFFE6E8E6)),
    NEUTRAL_800(Color(0xFF1F1F1F)),
    NEUTRAL_900(Color(0xFF060409)),

    GREEN_200(Color(0xFF9BAD50)),
    GREEN_600(Color(0xff556842)),
    BLUE_200(Color(0xFF849DBB)),
    BLUE_700(Color(0xFF122C4F)),

    YELLOW_200(Color(0xFFF5E08A)),
    ORANGE_600(Color(0xFFC1440E)),
    RED_500(Color(0xFF9E2A2B)),
    RED_900(Color(0xFF5D0D18)),

    PINK_200(Color(0xFFF4C9D6)),
    PURPLE_600(Color(0xFF51375B)),
    BROWN_400(Color(0xFF766153)),
    BROWN_800(Color(0xFF432818))
}
