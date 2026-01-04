package com.cabovianco.papelito.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.cabovianco.papelito.domain.model.Note
import com.cabovianco.papelito.domain.model.NoteColor

@Entity(tableName = "notes")
data class NoteEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val text: String,
    val backgroundColor: NoteColor,
    val fontColor: NoteColor
)

fun NoteEntity.toModel() = Note(id, text, backgroundColor, fontColor)
