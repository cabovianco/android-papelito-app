package com.cabovianco.papelito.domain.repository

import com.cabovianco.papelito.domain.model.Note
import kotlinx.coroutines.flow.Flow

interface NoteRepository {
    suspend fun add(note: Note)

    fun getAll(): Flow<List<Note>>

    fun getById(id: Long): Flow<Note?>

    suspend fun update(note: Note)

    suspend fun delete(note: Note)
}
