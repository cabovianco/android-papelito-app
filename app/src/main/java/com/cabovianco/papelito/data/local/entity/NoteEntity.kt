package com.cabovianco.papelito.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.cabovianco.papelito.domain.model.note.Note
import com.cabovianco.papelito.domain.model.note.NoteColor
import com.cabovianco.papelito.domain.model.note.NoteFontFamily
import com.cabovianco.papelito.domain.model.note.NoteFontWeight

@Entity(tableName = "notes")
data class NoteEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val text: String,
    val backgroundColor: NoteColor,
    val fontColor: NoteColor,
    val fontSize: Float,
    val fontWeight: NoteFontWeight,
    val fontFamily: NoteFontFamily
)

fun NoteEntity.toModel() =
    Note(id, text, backgroundColor, fontColor, fontSize, fontWeight, fontFamily)
