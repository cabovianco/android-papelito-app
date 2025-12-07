package com.cabovianco.papelito.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.cabovianco.papelito.data.local.entity.NoteEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {
    @Insert
    suspend fun insert(note: NoteEntity)

    @Query("SELECT * FROM notes WHERE notes.id = :id")
    fun getById(id: Long): Flow<NoteEntity?>

    @Query(
        """
        SELECT notes.id, notes.text, notes.backgroundColor, notes.fontColor
        FROM notes
        INNER JOIN notes_tags ON (notes.id = notes_tags.noteId AND notes_tags.tagId = :id)
    """
    )
    fun getByTagId(id: Long): Flow<List<NoteEntity>>

    @Query("SELECT * FROM notes")
    fun getAll(): Flow<List<NoteEntity>>

    @Update
    suspend fun update(note: NoteEntity)

    @Delete
    suspend fun delete(note: NoteEntity)
}
