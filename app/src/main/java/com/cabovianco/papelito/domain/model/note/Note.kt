package com.cabovianco.papelito.domain.model.note

import com.cabovianco.papelito.data.local.entity.NoteEntity

data class Note(
    val id: Long = 0L,
    val text: String,
    val backgroundColor: NoteColor,
    val fontColor: NoteColor,
    val fontSize: Float,
    val fontWeight: NoteFontWeight,
    val fontFamily: NoteFontFamily
)

fun Note.toEntity() =
    NoteEntity(id, text, backgroundColor, fontColor, fontSize, fontWeight, fontFamily)
