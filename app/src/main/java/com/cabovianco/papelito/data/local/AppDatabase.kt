package com.cabovianco.papelito.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.cabovianco.papelito.data.local.dao.NoteDao
import com.cabovianco.papelito.data.local.dao.TagDao
import com.cabovianco.papelito.data.local.entity.NoteEntity
import com.cabovianco.papelito.data.local.entity.NoteTagEntity
import com.cabovianco.papelito.data.local.entity.TagEntity

@Database(entities = [NoteEntity::class, TagEntity::class, NoteTagEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun provideNoteDao(): NoteDao
    abstract fun provideTagDao(): TagDao
}
