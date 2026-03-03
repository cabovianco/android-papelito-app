package com.cabovianco.papelito.data.repository

import com.cabovianco.papelito.data.local.dao.NoteDao
import com.cabovianco.papelito.data.local.entity.toModel
import com.cabovianco.papelito.domain.model.AppError
import com.cabovianco.papelito.domain.model.Result
import com.cabovianco.papelito.domain.model.err
import com.cabovianco.papelito.domain.model.note.Note
import com.cabovianco.papelito.domain.model.note.toEntity
import com.cabovianco.papelito.domain.model.ok
import com.cabovianco.papelito.domain.repository.NoteRepository
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

class NoteRepositoryImpl @Inject constructor(
    private val noteDao: NoteDao
) : NoteRepository {
    override fun observeAll(): Flow<Result<List<Note>>> =
        noteDao.observeAll()
            .map { entities -> ok(entities.map { it.toModel() }) }
            .catch { emit(err(AppError.DatabaseError.Unknown)) }

    override fun observeById(id: Long): Flow<Result<Note>> =
        noteDao.observeById(id)
            .map {
                it?.let { entity -> ok(entity.toModel()) }
                    ?: err(AppError.DatabaseError.NotFound)
            }
            .catch { emit(err(AppError.DatabaseError.Unknown)) }

    override suspend fun add(note: Note): Result<Unit> =
        executeSafely { ok(noteDao.insert(note.toEntity())) }

    override suspend fun update(note: Note): Result<Unit> =
        executeSafely {
            when (noteDao.update(note.toEntity())) {
                0 -> err(AppError.DatabaseError.NotFound)
                else -> ok(Unit)
            }
        }

    override suspend fun delete(note: Note): Result<Unit> =
        executeSafely {
            when (noteDao.delete(note.toEntity())) {
                0 -> err(AppError.DatabaseError.NotFound)
                else -> ok(Unit)
            }
        }
}

private suspend fun <T> executeSafely(block: suspend () -> Result<T>): Result<T> =
    try {
        block()
    } catch (_: Throwable) {
        Result.Err(AppError.DatabaseError.Unknown)
    }
