package com.cabovianco.papelito.domain.model

import com.cabovianco.papelito.data.local.entity.NoteEntity

data class Note(
    val id: Long = 0,
    val text: String,
)

fun Note.toEntity() = NoteEntity(id, text)
