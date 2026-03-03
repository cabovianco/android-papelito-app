package com.cabovianco.papelito.data.local.converter

import androidx.room.TypeConverter
import com.cabovianco.papelito.domain.model.note.NoteColor


class NoteColorConverter {
    @TypeConverter
    fun fromNoteColor(value: NoteColor): String = value.name

    @TypeConverter
    fun toNoteColor(name: String): NoteColor =
        NoteColor.entries.firstOrNull { it.name == name }
            ?: NoteColor.WHITE
}
