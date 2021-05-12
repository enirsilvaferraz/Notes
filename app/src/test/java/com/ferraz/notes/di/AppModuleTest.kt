package com.ferraz.notes.di

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.ferraz.notes.NotesDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent

@Module
@InstallIn(ApplicationComponent::class)
object AppModuleTest {

    @Provides
    fun provideContext() = ApplicationProvider.getApplicationContext<Context>()

    @Provides
    fun provideDatabase(context: Context) =
        Room.inMemoryDatabaseBuilder(context, NotesDatabase::class.java).build()

    @Provides
    fun provideNotesDao(database: NotesDatabase) = database.getNotesDao()
}