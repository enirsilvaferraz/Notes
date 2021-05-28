package com.ferraz.notes.repositories

import android.os.Build
import com.ferraz.notes.database.NotesDao
import com.ferraz.notes.database.NotesEntity
import com.ferraz.notes.di.DaoModuleProvidesTest
import dagger.hilt.android.testing.*
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import javax.inject.Inject

@UninstallModules(DaoModuleProvidesTest::class)
@HiltAndroidTest
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [Build.VERSION_CODES.Q], application = HiltTestApplication::class)
class NotesRepositoryTest : TestCase() {

    /**
     * Usado para injetar os componentes do Hilt
     */
    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @BindValue
    @JvmField
    var dao: NotesDao = mockk()

    @Inject
    lateinit var repository: NotesRepository

    /**
     * Ao iniciar cada teste regerar novas intancias do banco de dados
     */
    @Before
    fun init() {
        hiltRule.inject()
    }

    @Test
    fun `DADO que nao tenho registros salvos QUANDO busco todos os registros ENTAO deve retornar uma lista vazia`() = runBlocking {

        // DADO
        coEvery { dao.getAll() } returns listOf()

        // QUANDO
        val notes = repository.getNotes()

        // ENTAO
        assert(notes.isEmpty())
    }

    @Test
    fun `DADO que tenho registros salvos QUANDO busco todos os registros ENTAO deve retornar uma lista preenchida`() = runBlocking {

        // DADO
        coEvery { dao.getAll() } returns listOf(NotesEntity(1, "Nota 1"), NotesEntity(2, "Nota 2"), NotesEntity(3, "Nota 3"))

        // QUANDO
        val notes = repository.getNotes()

        // ENTAO
        assert(notes.isNotEmpty())
    }
}