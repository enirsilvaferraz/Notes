package com.ferraz.notes.di

import android.content.Context
import androidx.room.Room
import com.ferraz.notes.database.NotesDao
import com.ferraz.notes.database.NotesDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(ViewModelComponent::class)
object ViewModelModule {

    @Provides
    fun provideNotesDao(database: NotesDatabase): NotesDao = database.getNotesDao()
}

@Module
@InstallIn(SingletonComponent::class)
object ApplicationModule {

    @Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext appContext: Context): NotesDatabase =
        Room.databaseBuilder(appContext, NotesDatabase::class.java, "database").build()
}