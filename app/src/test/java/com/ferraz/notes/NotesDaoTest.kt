package com.ferraz.notes

import android.content.Context
import android.os.Build.VERSION_CODES.Q
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import junit.framework.TestCase
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import java.io.IOException

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [Q])
class NotesDaoTest : TestCase() {

    private lateinit var dao: NotesDao
    private lateinit var db: NotesDatabase

    @Before
    fun createDB() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, NotesDatabase::class.java).build()
        dao = db.getNotesDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDB() {
        db.close()
    }

    @Test
    fun `Deve retornar uma lista vazia de cartoes`() = runBlocking {

        val all = dao.getAll()

        assert(all.isNullOrEmpty())
    }

    @Test
    fun `Deve retornar uma lista contendo um cartao`() = runBlocking {

        dao.insert(NotesEntity(description = "Cartao 1"))

        val all = dao.getAll()

        assert(!all.isNullOrEmpty())
        assert(all.first().description == "Cartao 1")
    }

    @Test
    fun `Deve retornar uma lista contendo tres cartoes`() = runBlocking {

        dao.insert(
            NotesEntity(description = "Cartao 1"),
            NotesEntity(description = "Cartao 2"),
            NotesEntity(description = "Cartao 3")
        )

        val all = dao.getAll()

        assert(!all.isNullOrEmpty())
        assert(all[0].description == "Cartao 1")
        assert(all[1].description == "Cartao 2")
        assert(all[2].description == "Cartao 3")
    }

    @Test
    fun `Deve retornar uma lista vazia apos deletar um cartao`() = runBlocking {

        dao.insert(NotesEntity(description = "Cartao 1"))
        val inserted = dao.getAll().first()
        dao.delete(inserted)

        val all = dao.getAll()

        assert(all.isNullOrEmpty())
    }

    @Test
    fun `Deve retornar uma lista vazia apos deletar dois cartoes`() = runBlocking {

        dao.insert(NotesEntity(description = "Cartao 1"))
        dao.insert(NotesEntity(description = "Cartao 2"))
        dao.delete(*dao.getAll().toTypedArray())

        val all = dao.getAll()

        assert(all.isNullOrEmpty())
    }

    @Test
    fun `Deve retornar uma lista com um cartao atualizado`() = runBlocking {

        dao.insert(NotesEntity(description = "Cartao 1"))
        val inserted = dao.getAll().first()
        dao.update(inserted.copy(description = "Cartao 2"))

        val all = dao.getAll()

        assert(!all.isNullOrEmpty())
        assert(all.first().description == "Cartao 2")
    }

    @Test
    fun `Deve retornar uma lista com dois cartoes atualizados`() = runBlocking {

        dao.insert(
            NotesEntity(description = "Cartao 1"),
            NotesEntity(description = "Cartao 2")
        )

        dao.update(
            dao.getAll()[0].copy(description = "Cartao 1 Atualizado"),
            dao.getAll()[1].copy(description = "Cartao 2 Atualizado")
        )

        val all = dao.getAll()

        assert(!all.isNullOrEmpty())
        assert(all[0].description == "Cartao 1 Atualizado")
        assert(all[1].description == "Cartao 2 Atualizado")
    }
}