package com.ferraz.notes

import androidx.room.*

@Dao
interface NotesDao {

    @Insert
    suspend fun insert(vararg entities: NotesEntity)

    @Delete
    suspend fun delete(vararg entities: NotesEntity)

    @Update
    suspend fun update(vararg entities: NotesEntity)

    @Query("SELECT * FROM NOTES")
    suspend fun getAll(): List<NotesEntity>
}
