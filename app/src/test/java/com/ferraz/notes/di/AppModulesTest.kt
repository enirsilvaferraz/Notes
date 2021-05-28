package com.ferraz.notes.di

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.ferraz.notes.database.NotesDao
import com.ferraz.notes.database.NotesDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DaoModuleProvidesTest {

    @Singleton
    @Provides
    fun provideContext(): Context = ApplicationProvider.getApplicationContext()

    @Singleton
    @Provides
    fun provideDatabase(context: Context): NotesDatabase = Room.inMemoryDatabaseBuilder(context, NotesDatabase::class.java).build()

    @Singleton
    @Provides
    fun provideNotesDao(database: NotesDatabase): NotesDao = database.getNotesDao()
}

/*
@Module
@InstallIn(ViewModelComponent::class)
abstract class RepositoryModuleBindsTest {

    @Binds
    abstract fun provideNotesRepositoryLocal(localRepository: NotesRepositoryLocal): NotesRepository
}
 */