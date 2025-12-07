package com.cabovianco.papelito.data.local.converter

import androidx.room.TypeConverter
import com.cabovianco.papelito.domain.model.Color


class ColorConverter {
    @TypeConverter
    fun fromColor(value: Color): String = value.name

    @TypeConverter
    fun toColor(value: String): Color = Color.valueOf(value)
}
