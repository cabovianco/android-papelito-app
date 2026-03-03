package com.cabovianco.papelito.data.local.converter

import androidx.room.TypeConverter
import com.cabovianco.papelito.domain.model.note.NoteFontWeight

class NoteFontWeightConverter {
    @TypeConverter
    fun fromNoteFontWeight(value: NoteFontWeight): String = value.name

    @TypeConverter
    fun toNoteFontWeight(name: String): NoteFontWeight =
        NoteFontWeight.entries.firstOrNull { it.name == name }
            ?: NoteFontWeight.NORMAL
}
