package com.ferraz.notes.di

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.ferraz.notes.database.NotesDao
import com.ferraz.notes.database.NotesDatabase
import com.ferraz.notes.repositories.NotesRepository
import com.ferraz.notes.repositories.NotesRepositoryLocal
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object DaoModuleProvidesTest {

    @Provides
    fun provideContext(): Context = ApplicationProvider.getApplicationContext<Context>()

    @Provides
    fun provideDatabase(context: Context): NotesDatabase = Room.inMemoryDatabaseBuilder(context, NotesDatabase::class.java).build()

    @Provides
    fun provideNotesDao(database: NotesDatabase): NotesDao = database.getNotesDao()
}

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModuleBindsTest {

    @Binds
    abstract fun provideNotesRepositoryLocal(localRepository: NotesRepositoryLocal): NotesRepository
}