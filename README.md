# Projeto de Bloco de Notas

# 1. Testando recursos do banco de dados usando ROOM e Hilt

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

Algumas configurações são necessárias para rodar esse tipo de teste e prover a instância do banco de dados é uma delas. Optei pelo uso da injeção de dependêcmcias para simplificar o uso das intâncias do banco de dados nos testes.

<BR>

### Hilt

A configuração do Hilt para injeção de depências é bastante simples. Como a instância do banco de dados pertence a uma lib de terceiros não é possível anotar seu construtor com @Injet e portanto utilizei a anotação @Provide para recuperar os dados que preciso.

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

### ROOM

A partir desse momento é necessário criar as classes **NotesDatabase**, **NotesDao** e **NotesEntity**.

```kotlin
abstract class NotesDatabase : RoomDatabase() {
    abstract fun getNotesDao(): NotesDao
}
```

```kotlin
interface NotesDao 
```

```kotlin
class NotesEntity
```

<BR>

### Classe de Teste

Na classe de testes devemos realizar algumas configurações também. 

* Para utilizar o Hilt é necessário anotar a classe de teste com **@HiltAndroidTest** e utilizar a aplicação **HiltTestApplication::class** para rodar os testes.
* Para obter o **Context** via **ApplicationProvider.getApplicationContext<Context>()** é necessário fazer uso do containers que possuam contexto, como é o caso do **Robolectric**.
* No método **init()** anotado com **@Before** solicitamos ao Hilt que intancie as dependências (anotadas com **@Inject**) a cada execução de testes.
* No método **closeDB()** solicitamos ao ROOM que feche o banco de dados ao final das execuções de teste.

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

## Implementando o Primeiro Teste

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

* **DADO que nao tenho notas cadastradas**: o banco de dados inicia-se zerado, portanto não há necessidade de deletar registros anteriormente adicionados.
* **QUANDO busco um unico registro**: agora é hora de executar a ação de buscar e para isso devemos criar na interface NotesDao o método que faz isso.
* **ENTAO nao devera retornar registros**: o resultado deve ser nulo, uma vez que não haja registros no banco de dados. 

### Finalizando a Configuração do ROOM

As configurações do ROOM virão a medida que os testes forem pedindo. No primeiro teste necessitamos criar o método **dao.getByID(1)**. Faremos da seguinte forma:

A classe **NotesDao** foi anotada com **@Dao** e o método de consulta anotado com **@Query** devidamente configurado.

```kotlin
@Dao
interface NotesDao {

    @Query("SELECT * FROM NOTES WHERE uid = :uid")
    suspend fun getByID(uid: Int): NotesEntity?
}
```

Agora, fez-se necessário configurar a classe **NotesEntity** para que o ROOM a converta em uma tabela no SQLite. A classe foi anotada com **@Entity** especificando o nome da tablea (mesmo nome usado na **@Query**). A proriedade **uid** será a chave da tabela e será auto gerada e a propriedade **description** foi anotada com **@ColumnInfo** contendo as informações da coluna.

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

Pronto! Bora rodar esse teste!

## Implementando demais Testes

Os demais testes podem ser encontrados na classe **NotesDaoTest** disponível no GitHub.