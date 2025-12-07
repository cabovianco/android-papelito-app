package com.cabovianco.papelito.domain.repository

import com.cabovianco.papelito.domain.model.Note
import kotlinx.coroutines.flow.Flow

interface NoteRepository {
    fun getAll(): Flow<List<Note>>
    fun getById(id: Long): Flow<Note?>
    suspend fun add(note: Note): Result<Unit>
    suspend fun update(note: Note): Result<Unit>
    suspend fun delete(note: Note): Result<Unit>
}
