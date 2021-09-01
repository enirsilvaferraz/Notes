package com.ferraz.notes.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface NotesDao {

    @Insert
    suspend fun insert(vararg entities: NotesEntity)

    @Delete
    suspend fun delete(vararg entities: NotesEntity)

    @Update
    suspend fun update(vararg entities: NotesEntity)

    @Query("SELECT * FROM NOTES")
    fun getAll(): LiveData<List<NotesEntity>>

    @Query("SELECT * FROM NOTES WHERE uid = :uid")
    fun getByID(uid: Int): NotesEntity?
}
