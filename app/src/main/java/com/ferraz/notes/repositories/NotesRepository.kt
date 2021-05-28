package com.ferraz.notes.repositories

import com.ferraz.notes.database.NotesDao
import com.ferraz.notes.database.NotesEntity
import javax.inject.Inject

class NotesRepository @Inject constructor(private val dao: NotesDao) {

    suspend fun getNotes(): List<NotesEntity> = dao.getAll()
}