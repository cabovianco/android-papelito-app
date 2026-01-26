package com.cabovianco.papelito.data.local.converter

import androidx.room.TypeConverter
import com.cabovianco.papelito.domain.model.NoteFontFamily

class NoteFontFamilyConverter {
    @TypeConverter
    fun fromNoteFontFamily(value: NoteFontFamily): String = value.name

    @TypeConverter
    fun toNoteFontFamily(name: String): NoteFontFamily =
        NoteFontFamily.entries.firstOrNull { it.name == name }
            ?: NoteFontFamily.SANS_SERIF
}
