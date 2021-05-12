package com.ferraz.notes.di

/*
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

 */