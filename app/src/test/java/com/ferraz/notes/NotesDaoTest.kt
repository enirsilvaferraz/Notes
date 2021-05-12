package com.ferraz.notes

import android.os.Build.VERSION_CODES.Q
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import dagger.hilt.android.testing.UninstallModules
import junit.framework.TestCase
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import java.io.IOException
import javax.inject.Inject

/**
 * @UninstallModules(AppModule::class) -> Remove o modulo hilt da aplicação e passa a usar o modulo de teste
 * @HiltAndroidTest -> Indica que pode-se usar injeção de dependencias Hilt nos testes unitários
 * @RunWith(RobolectricTestRunner::class) -> Uso do Robolectric para obter o context via Provider
 * @Config(sdk = [Q], application = HiltTestApplication::class) -> Configurações do Robolectric e uso do HiltTestApplication
 */

@UninstallModules(AppModule::class)
@HiltAndroidTest
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [Q], application = HiltTestApplication::class)
class NotesDaoTest : TestCase() {

    /**
     * Usado para injetar os componentes do Hilt
     */
    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var db: NotesDatabase

    @Inject
    lateinit var dao: NotesDao

    /**
     * Ao iniciar cada teste regerar as intancias do banco de dados
     */
    @Before
    fun init() {
        hiltRule.inject()
    }

    /**
     * Fechar o banco ao terminar os testes
     */
    @After
    @Throws(IOException::class)
    fun closeDB() {
        db.close()
    }

    @Test
    fun `DADO que nao cadastrei notas QUANDO busca todos os registros ENTAO deve retornar uma lista vazia`() = runBlocking {

        // QUANDO
        val all = dao.getAll()

        // ENTAO
        assert(all.isNullOrEmpty())
    }

    @Test
    fun `DADO que cadastrei uma nota QUANDO busco todos os registros ENTAO deve retornar uma lista contendo a nota`() = runBlocking {

        // DADO
        dao.insert(NotesEntity(description = "Cartao 1"))

        // QUANDO
        val all = dao.getAll()

        // ENTAO
        assert(!all.isNullOrEmpty())
        assert(all.first().description == "Cartao 1")
    }

    @Test
    fun `DADO que cadastrei tres notas QUANDO busco todos os registros ENTAO deve retornar uma lista contendo as tres notas`() = runBlocking {

        // DADO
        dao.insert(
            NotesEntity(description = "Cartao 1"),
            NotesEntity(description = "Cartao 2"),
            NotesEntity(description = "Cartao 3")
        )

        // QUANDO
        val all = dao.getAll()

        // ENTAO
        assert(!all.isNullOrEmpty())
        assert(all[0].description == "Cartao 1")
        assert(all[1].description == "Cartao 2")
        assert(all[2].description == "Cartao 3")
    }

    @Test
    fun `DADO que cadastrei uma unica nota QUANDO deleto essa nota ENTAO deve retornar uma lista vazia`() = runBlocking {

        // DADO
        dao.insert(NotesEntity(description = "Cartao 1"))
        val inserted = dao.getAll().first()

        // QUANDO
        dao.delete(inserted)

        // ENTAO
        val all = dao.getAll()
        assert(all.isNullOrEmpty())
    }

    @Test
    fun `DADO que cadastrei duas notas QUNDO deleto essas notas ENTAO deve retornar uma lista vazia`() = runBlocking {

        // DADO
        dao.insert(NotesEntity(description = "Cartao 1"))
        dao.insert(NotesEntity(description = "Cartao 2"))

        // QUANDO
        dao.delete(*dao.getAll().toTypedArray())

        // ENTAO
        val all = dao.getAll()
        assert(all.isNullOrEmpty())
    }

    @Test
    fun `DADO que cadastrei duas notas QUNDO deleto uma delas ENTAO deve retornar uma lista com a nota nao deletada`() = runBlocking {

        // DADO
        dao.insert(NotesEntity(description = "Cartao 1"))
        dao.insert(NotesEntity(description = "Cartao 2"))

        // QUANDO
        dao.delete(dao.getAll().first())

        // ENTAO
        val all = dao.getAll()
        assert(!all.isNullOrEmpty())
        assert(all[0].description == "Cartao 2")
    }

    @Test
    fun `DADO que cadatrei uma nota QUANDO atualizo essa nota ENTAO deve retornar uma lista com a nota atualizada`() = runBlocking {

        // DADO
        dao.insert(NotesEntity(description = "Cartao 1"))

        // QUANDO
        val inserted = dao.getAll().first()
        dao.update(inserted.copy(description = "Cartao 2"))

        // ENTAO
        val all = dao.getAll()
        assert(!all.isNullOrEmpty())
        assert(all.first().description == "Cartao 2")
    }

    @Test
    fun `DADO que cadastrei duas notas QUANDO atualizo essas notas ENTAO deve retornar uma lista com duas notas atualizadas`() = runBlocking {

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