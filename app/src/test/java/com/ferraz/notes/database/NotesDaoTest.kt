package com.ferraz.notes.database

import android.database.sqlite.SQLiteConstraintException
import android.os.Build.VERSION_CODES.Q
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
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
 * =================================================================================================
 * TEST CASE: Testar insersao, atualizacao, delecao e consulta de notas no banco de dados
 * =================================================================================================
 *
 * @UninstallModules(AppModule::class) -> Remove o modulo hilt da aplicação e passa a usar o modulo de teste
 * @HiltAndroidTest -> Indica que pode-se usar injeção de dependencias Hilt nos testes unitários
 * @RunWith(RobolectricTestRunner::class) -> Uso do Robolectric para obter o context via Provider
 * @Config(sdk = [Q], application = HiltTestApplication::class) -> Configurações do Robolectric e uso do HiltTestApplication
 */

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
     * Ao iniciar cada teste regerar novas intancias do banco de dados
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

    /**
     * Recuperacao
     */

    @Test
    fun `DADO que nao tenho notas cadastradas QUANDO busco um unico registro ENTAO nao devera retornar registros`() = runBlocking {

        // QUANDO
        val register = dao.getByID(1)

        // ENTAO
        assert(register == null)
    }

    @Test
    fun `DADO que nao tenho notas cadastradas QUANDO busco todos os registros ENTAO nao devera retornar registros`() = runBlocking {

        // QUANDO
        val all = dao.getAll()

        // ENTAO
        assert(all.isNullOrEmpty())
    }

    @Test
    fun `DADO que tenho notas cadastradas QUANDO busco um unico registro ENTAO devera retornar o registro`() = runBlocking {

        // DADO
        dao.insert(
            NotesEntity(description = "Cartao 1"),
            NotesEntity(description = "Cartao 2")
        )

        // QUANDO
        val register = dao.getByID(1)

        // ENTAO
        assert(register != null)
        assert(register!!.description == "Cartao 1")
    }

    @Test
    fun `DADO que tenho notas cadastradas QUANDO busco todos os registros ENTAO devera retornar registros`() = runBlocking {

        // DADO
        dao.insert(
            NotesEntity(description = "Cartao 1"),
            NotesEntity(description = "Cartao 2")
        )

        // QUANDO
        val all = dao.getAll()

        // ENTAO
        assert(!all.isNullOrEmpty())
        assert(all[0].description == "Cartao 1")
        assert(all[1].description == "Cartao 2")
    }

    /**
     * Insercao
     */

    @Test
    fun `DADO que nao tenho notas cadastradas QUANDO cadastro um unico registro ENTAO ele devera ser persistido no banco de dados`() = runBlocking {

        // QUANDO
        dao.insert(NotesEntity(description = "Cartao 1"))

        // ENTAO
        val register = dao.getByID(dao.getAll()[0].uid!!)
        assert(register != null)
        assert(register!!.description == "Cartao 1")
    }

    @Test
    fun `DADO que nao tenho notas cadastradas QUANDO cadastro varios registros ENTAO eles deverao ser persistidos no banco de dados`() = runBlocking {

        // QUANDO
        dao.insert(
            NotesEntity(description = "Cartao 1"),
            NotesEntity(description = "Cartao 2"),
            NotesEntity(description = "Cartao 3")
        )

        // ENTAO
        val all = dao.getAll()
        assert(!all.isNullOrEmpty())
        assert(all[0].description == "Cartao 1")
        assert(all[1].description == "Cartao 2")
        assert(all[2].description == "Cartao 3")
    }

    @Test(expected = SQLiteConstraintException::class)
    fun `DADO que tenho notas cadastradas QUANDO cadastro um unico registro que ja existe ENTAO ele devera retornar erro`() = runBlocking {

        // DADO
        dao.insert(
            NotesEntity(description = "Cartao 1"),
            NotesEntity(description = "Cartao 2")
        )

        // QUANDO
        dao.insert(NotesEntity(uid = 1, description = "Cartao 1"))
    }


    /**
     * Delecao
     */

    @Test
    fun `DADO que nao tenho notas cadastradas QUANDO deleto um unico registro ENTAO nao devera alterar os registros do banco de dados`() = runBlocking {

        // QUANDO
        dao.delete(NotesEntity(uid = 1, description = "Cartao 1"))

        // ENTAO
        val all = dao.getAll()
        assert(all.isNullOrEmpty())
    }

    @Test
    fun `DADO que tenho notas cadastradas QUANDO deleto um unico registro ENTAO ele devera ser deletado do banco de dados`() = runBlocking {

        // DADO
        dao.insert(
            NotesEntity(description = "Cartao 1"),
            NotesEntity(description = "Cartao 2")
        )
        val inserted = dao.getAll().first()

        // QUANDO
        dao.delete(inserted)

        // ENTAO
        val all = dao.getAll()
        assert(!all.isNullOrEmpty())
        assert(!all.contains(inserted))
    }

    @Test
    fun `DADO que tenho notas cadastradas QUANDO deleto todos os registros ENTAO eles deverao ser deletados do banco de dados`() = runBlocking {

        // DADO
        dao.insert(
            NotesEntity(description = "Cartao 1"),
            NotesEntity(description = "Cartao 2"),
            NotesEntity(description = "Cartao 3")
        )

        // QUANDO
        dao.delete(*dao.getAll().toTypedArray())

        // ENTAO
        val all = dao.getAll()
        assert(all.isNullOrEmpty())
    }

    /**
     * Atualizacap
     */

    @Test
    fun `DADO que nao tenho notas cadastradas QUANDO atualizo um unico registro ENTAO nao devera alterar os registros do banco de dados`() = runBlocking {

        // QUANDO
        dao.update(NotesEntity(uid = 1, description = "Cartao 1"))

        // ENTAO
        val all = dao.getAll()
        assert(all.isNullOrEmpty())
    }

    @Test
    fun `DADO que tenho notas cadastradas QUANDO atualizo um registro ENTAO ele devera ser atualizado no banco de dados`() = runBlocking {

        // DADO
        dao.insert(NotesEntity(description = "Cartao 1"))

        // QUANDO
        dao.getAll()[0].also {
            dao.update(it.copy(description = "Cartao 1 Atualizado"))
        }

        // ENTAO
        val registers = dao.getAll()
        assert(!registers.isNullOrEmpty())
        assert(registers[0].description == "Cartao 1 Atualizado")

    }

    @Test
    fun `DADO que tenho notas cadastradas QUANDO atualizo todos os registros ENTAO eles deverao ser atualizados no banco de dados`() = runBlocking {

        // DADO
        dao.insert(
            NotesEntity(description = "Cartao 1"),
            NotesEntity(description = "Cartao 2")
        )

        // QUANDO
        dao.update(
            dao.getAll()[0].copy(description = "Cartao 1 Atualizado"),
            dao.getAll()[1].copy(description = "Cartao 2 Atualizado")
        )

        // ENTAO
        val all = dao.getAll()
        assert(!all.isNullOrEmpty())
        assert(all[0].description == "Cartao 1 Atualizado")
        assert(all[1].description == "Cartao 2 Atualizado")
    }

}