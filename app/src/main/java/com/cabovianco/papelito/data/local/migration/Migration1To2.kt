package com.cabovianco.papelito.data.local.migration

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATION_1_2 = object: Migration(1, 2) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("""
            ALTER TABLE notes
            ADD COLUMN fontSize REAL NOT NULL DEFAULT 16.0
        """.trimIndent())

        db.execSQL("""
            ALTER TABLE notes
            ADD COLUMN fontWeight TEXT NOT NULL DEFAULT 'NORMAL'
        """.trimIndent())

        db.execSQL("""
            ALTER TABLE notes
            ADD COLUMN fontFamily TEXT NOT NULL DEFAULT 'DEFAULT'
        """.trimIndent())

        db.execSQL("CREATE INDEX IF NOT EXISTS index_notes_tags_noteId ON notes_tags(noteId)")

        db.execSQL("CREATE INDEX IF NOT EXISTS index_notes_tags_tagId ON notes_tags(tagId)")
    }
}
