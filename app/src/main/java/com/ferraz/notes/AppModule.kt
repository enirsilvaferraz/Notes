package com.ferraz.notes

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object AppModule {

    @Provides
    fun provideNotesDao(database: NotesDatabase): NotesDao = database.getNotesDao()

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext appContext: Context): NotesDatabase =
        Room.databaseBuilder(appContext, NotesDatabase::class.java, "database").build()
}