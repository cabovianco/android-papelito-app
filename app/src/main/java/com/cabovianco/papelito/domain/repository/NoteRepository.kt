package com.cabovianco.papelito.domain.repository

import com.cabovianco.papelito.domain.model.Result
import com.cabovianco.papelito.domain.model.note.Note
import kotlinx.coroutines.flow.Flow

interface NoteRepository {
    fun observeAll(): Flow<Result<List<Note>>>
    fun observeById(id: Long): Flow<Result<Note>>
    suspend fun add(note: Note): Result<Unit>
    suspend fun update(note: Note): Result<Unit>
    suspend fun delete(note: Note): Result<Unit>
}
