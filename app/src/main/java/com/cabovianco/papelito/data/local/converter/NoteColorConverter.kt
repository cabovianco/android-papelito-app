package com.cabovianco.papelito.data.local.converter

import androidx.room.TypeConverter
import com.cabovianco.papelito.domain.model.NoteColor


class NoteColorConverter {
    @TypeConverter
    fun fromNoteColor(value: NoteColor): String = value.name

    @TypeConverter
    fun toNoteColor(value: String): NoteColor = NoteColor.valueOf(value)
}
