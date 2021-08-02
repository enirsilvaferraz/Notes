package com.ferraz.notes.views

import android.os.Build
import com.ferraz.notes.repositories.NotesRepository
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import junit.framework.TestCase
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import javax.inject.Inject

@HiltAndroidTest
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [Build.VERSION_CODES.Q], application = HiltTestApplication::class)
class NoteListViewModelTest : TestCase() {

    /**
     * Usado para injetar os componentes do Hilt
     */
    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    /**
     * Ao iniciar cada teste regerar novas intancias do banco de dados
     */
    @Before
    fun init() {
        hiltRule.inject()
    }

    @BindValue
    var repository: NotesRepository = mockk()

    @Inject
    lateinit var viewModel: NoteListViewModel

    @Test
    fun `DADO que abri a tela de listagem de notas ENTAO devo solicitar a tela que as exiba`() {

        coEvery { repository.getNotes() } returns emptyList()

        // DADO
        viewModel.onStart()

        // ENTAO
        coVerify(exactly = 1) { repository.getNotes() }
        /*
        viewModel.notes.observe(){
            when (it.getState()) {
                NoteListViewModel.State.LOADING -> TODO()
                NoteListViewModel.State.SUCCESS -> TODO()
                NoteListViewModel.State.FAILURE -> TODO()
            }
        }
         */

    }
}