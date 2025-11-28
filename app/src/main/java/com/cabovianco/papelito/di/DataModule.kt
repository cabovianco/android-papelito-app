package com.cabovianco.papelito.di

import android.content.Context
import androidx.room.Room
import com.cabovianco.papelito.data.local.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
data object DataModule {
    private const val DATABASE_NAME = "papelito_db"

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context) = Room
        .databaseBuilder(context, AppDatabase::class.java, DATABASE_NAME)
        .build()

    @Provides
    fun provideNoteDao(db: AppDatabase) = db.provideNoteDao()

    @Provides
    fun provideTagDao(db: AppDatabase) = db.provideTagDao()
}
