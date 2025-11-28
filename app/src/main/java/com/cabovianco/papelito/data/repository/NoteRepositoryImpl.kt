package com.cabovianco.papelito.data.repository

import com.cabovianco.papelito.data.local.dao.NoteDao
import com.cabovianco.papelito.data.local.entity.toModel
import com.cabovianco.papelito.domain.model.Note
import com.cabovianco.papelito.domain.model.toEntity
import com.cabovianco.papelito.domain.repository.NoteRepository
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class NoteRepositoryImpl @Inject constructor(
    private val noteDao: NoteDao
) : NoteRepository {
    override suspend fun add(note: Note) {
        noteDao.insert(note.toEntity())
    }

    override fun getAll(): Flow<List<Note>> =
        noteDao.getAll().map { it.map { note -> note.toModel() } }

    override fun getById(id: Long): Flow<Note?> =
        noteDao.getById(id).map { it?.toModel() }

    override suspend fun update(note: Note) {
        noteDao.update(note.toEntity())
    }

    override suspend fun delete(note: Note) {
        noteDao.delete(note.toEntity())
    }
}
