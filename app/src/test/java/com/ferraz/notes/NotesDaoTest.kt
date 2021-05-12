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

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var db: NotesDatabase

    @Inject
    lateinit var dao: NotesDao

    @Before
    fun init() {
        hiltRule.inject()
    }

    @After
    @Throws(IOException::class)
    fun closeDB() {
        db.close()
    }

    @Test
    fun `DADO que nao cadastrei notas QUANDO busca todos os registros ENTAO deve retornar uma lista vazia`() = runBlocking {

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