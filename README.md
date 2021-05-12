# Projeto de Bloco de Notas

# 1. Testando recursos do banco de dados ROOM

<BR>

## 1.1. Definindo os casos de teste

Antes de começar a escrever o primeito teste precisamos definir nosso plano de testes, que é basicamente o que testar. Para banco de dados, é rasoável dizer que devemos testar a obtenção, insersão, atualização e deleção dos dados, por isso, chegamos aos seguintes casos de teste.


```kotlin
class NotesDaoTest : TestCase() {

    /**
     * Recuperacao
     */

    @Test
    fun `DADO que nao tenho notas cadastradas QUANDO busco um unico registro ENTAO nao devera retornar registros`() {}

    @Test
    fun `DADO que nao tenho notas cadastradas QUANDO busco todos os registros ENTAO nao devera retornar registros`() {}

    @Test
    fun `DADO que tenho notas cadastradas QUANDO busco um unico registro ENTAO devera retornar o registro`() {}

    @Test
    fun `DADO que tenho notas cadastradas QUANDO busco todos os registros ENTAO devera retornar registros`() {}

    /**
     * Insercao
     */

    @Test
    fun `DADO que nao tenho notas cadastradas QUANDO cadastro um unico registro ENTAO ele devera ser persistido no banco de dados`() {}

    @Test
    fun `DADO que nao tenho notas cadastradas QUANDO cadastro varios registros ENTAO eles deverao ser persistidos no banco de dados`() {}

    @Test(expected = SQLiteConstraintException::class)
    fun `DADO que tenho notas cadastradas QUANDO cadastro um unico registro que ja existe ENTAO ele devera retornar erro`() {}


    /**
     * Delecao
     */

    @Test
    fun `DADO que nao tenho notas cadastradas QUANDO deleto um unico registro ENTAO nao devera alterar os registros do banco de dados`() {}

    @Test
    fun `DADO que tenho notas cadastradas QUANDO deleto um unico registro ENTAO ele devera ser deletado do banco de dados`() {}

    @Test
    fun `DADO que tenho notas cadastradas QUANDO deleto todos os registros ENTAO eles deverao ser deletados do banco de dados`() {}

    /**
     * Atualizacap
     */

    @Test
    fun `DADO que nao tenho notas cadastradas QUANDO atualizo um unico registro ENTAO nao devera alterar os registros do banco de dados`() {}

    @Test
    fun `DADO que tenho notas cadastradas QUANDO atualizo um registro ENTAO ele devera ser atualizado no banco de dados`() {}

    @Test
    fun `DADO que tenho notas cadastradas QUANDO atualizo todos os registros ENTAO eles deverao ser atualizados no banco de dados`() {}
}
```

Note que usei o conceito DADO-QUANDO-ENTAO para priorizar a clareza e a compreenção dos casos de testes. A partir desse conceito, considera-se um cenário inicial (DADO), onde os dados serão preparados, que sofrerá uma ação (QUANDO), o que deve ser testado em si, e responderá com um resultado esperado (ENTAO).

Aproveite esses testes para conhecer as particularidades do banco e descobrir seu comportamento, como inserir uma conta já cadastrada ou deletar uma conta já deletada.

<BR>

## 1.2. Preparando a classe de teste

Algumas configurações são necessárias para rodar esse tipo de teste a começar pela própria classe de teste.


<BR>

### Hilt

Para garantir a manutenibilidade e facilitar o gerenciamento das dependências do projeto vamos trabalhar com o injetor de dependências Hilt. Sua configuração é bastante simples, como a instância do banco de dados pertence a uma lib de terceiros, não é possível anotar seu construtor com **@Injet** e, portanto, utilizei a anotação **@Provides** para recuperar os dados que preciso.

```kotlin
@Module
@InstallIn(ApplicationComponent::class)
object AppModuleTest {

    @Provides
    fun provideContext() = ApplicationProvider.getApplicationContext<Context>()

    @Provides
    fun provideDatabase(context: Context) = Room.inMemoryDatabaseBuilder(context, NotesDatabase::class.java).build()

    @Provides
    fun provideNotesDao(database: NotesDatabase) = database.getNotesDao()
}
```

<BR>

### Configuração do ROOM

A partir desse momento é necessário criar as classes **NotesDatabase**, **NotesDao** e **NotesEntity**. As configurações do ROOM podem vir a medida que os testes forem pedindo. No primeiro teste necessitamos criar o método **dao.getByID(1)**. Faremos da seguinte forma:

A classe **NotesDao** foi anotada com **@Dao** e o método de consulta anotado com **@Query** devidamente configurado.

```kotlin
@Dao
interface NotesDao {

    @Query("SELECT * FROM NOTES WHERE uid = :uid")
    suspend fun getByID(uid: Int): NotesEntity?
}
```

Agora, fez-se necessário configurar a classe **NotesEntity** para que o ROOM a converta em uma tabela no SQLite. A classe foi anotada com **@Entity** especificando o nome da tabela (mesmo nome usado na **@Query**). A proriedade **uid** será a chave da tabela e será auto gerada e a propriedade **description** foi anotada com **@ColumnInfo** contendo as informações da coluna.

```kotlin
@Entity(tableName = "NOTES")
data class NotesEntity(

    @PrimaryKey(autoGenerate = true) val uid: Int? = null,
    
    @ColumnInfo(name = "description") val description: String
)
```

Por fim, basta configurar a classe **NotesDatabase** com a tabela (NotesEntity) e a versão do banco (1).

```kotlin
@Database(entities = [NotesEntity::class], version = 1)
abstract class NotesDatabase : RoomDatabase() {
    abstract fun getNotesDao(): NotesDao
}
```

<BR>

### Classe de Teste

Para utilizar o Hilt é necessário anotar a classe de teste com **@HiltAndroidTest** e utilizar a aplicação **HiltTestApplication::class** para rodar os testes.

Além disso, devemos implementar o método **init()** anotado com **@Before** que solicita ao Hilt que intancie as dependências (anotadas com **@Inject**) a cada execução de testes. 

No método **closeDB()** solicitamos ao ROOM que feche o banco de dados ao final das execuções de teste.

Para obter o **Context** via **ApplicationProvider.getApplicationContext<Context>()** é necessário fazer uso de containers que possuam contexto, como é o caso do **Robolectric**. Note que na classe de teste existe a anotação **@RunWith(RobolectricTestRunner::class)** indicando essa configuração.

```kotlin
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

   // ... Testes ficarao logo abaixo
}
```

<BR>

### Implementando o Primeiro Teste

Seguindo a lista de testes, o primeiro diz respeito a consulta de dados e pode ser implementado da seguinte forma.

```kotlin
@Test
fun `DADO que nao tenho notas cadastradas QUANDO busco um unico registro ENTAO nao devera retornar registros`() = runBlocking {

    // QUANDO
    val register = dao.getByID(1)

    // ENTAO
    assert(register == null)
}
```

<BR>

* **DADO que nao tenho notas cadastradas**: o banco de dados inicia-se zerado, portanto não há necessidade de deletar registros anteriormente adicionados.
* **QUANDO busco um unico registro**: agora é hora de executar a ação de buscar e para isso devemos criar na interface NotesDao o método que faz isso.
* **ENTAO nao devera retornar registros**: o resultado deve ser nulo, uma vez que não haja registros no banco de dados. 

<BR>

Pronto! Bora rodar esse teste!

<BR>

## Implementando demais Testes

Os demais testes podem ser encontrados na classe **NotesDaoTest** (a seguir) disponível também no GitHub. 

```kotlin
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
```