package com.ferraz.notes.repositories

import com.ferraz.notes.database.NotesDao
import com.ferraz.notes.database.NotesEntity
import javax.inject.Inject

interface NotesRepository {

    suspend fun getNotes(): List<NotesEntity>
}

class NotesRepositoryLocal @Inject constructor(private val dao: NotesDao) : NotesRepository {

    override suspend fun getNotes(): List<NotesEntity> = dao.getAll()
}