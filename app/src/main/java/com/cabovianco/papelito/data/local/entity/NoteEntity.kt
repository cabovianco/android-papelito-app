package com.cabovianco.papelito.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.cabovianco.papelito.domain.model.Note

@Entity(tableName = "notes")
data class NoteEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val text: String,
    //val color: Color
)

fun NoteEntity.toModel() = Note(id, text)
