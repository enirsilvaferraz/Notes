package com.ferraz.notes.views

import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.material.BottomAppBar
import androidx.compose.material.BottomSheetValue.*
import androidx.compose.material.FabPosition
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.ferraz.notes.database.NotesEntity
import com.ferraz.notes.views.NoteListViewModel.NotesState.*
import com.ferraz.notes.views.ui.components.EmptyScreen
import com.ferraz.notes.views.ui.components.LoadingScreen
import com.ferraz.notes.views.ui.components.NoteCard
import com.ferraz.notes.views.ui.features.notes.register.NoteDialog
import com.ferraz.notes.views.ui.theme.NotesTheme
import com.ferraz.notes.views.ui.theme.StatusBar
import com.ferraz.notes.views.ui.theme.Typography
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NoteListActivity : AppCompatActivity() {

    private val notesVM: NoteListViewModel by viewModels()

    @ExperimentalUnitApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            notesVM.notes.observeAsState().value?.let { state ->
                NotesAppScreen(state)
            }
        }

        lifecycle.addObserver(notesVM)
    }

    @ExperimentalUnitApi
    @Preview(showBackground = true, name = "App Main Screen")
    @Composable
    fun NotesAppScreen(state: NoteListViewModel.NotesState = Success(data = MockHelper.items)) {

        NotesTheme {

            val openDialog = remember { mutableStateOf(false) }

            val floatingActionButton = @Composable {
                if (state == Empty || state is Success) NotesFAB {
                    openDialog.value = true
                }
            }

            val content: @Composable (PaddingValues) -> Unit = {
                when (state) {
                    is Loading -> LoadingScreen()
                    is Empty -> EmptyScreen()
                    is Success -> NoteGrid(state.data)
                    is Failure -> {
                        // do nothing
                    }
                }
            }

            StatusBar()

            Scaffold(
                topBar = { NotesTopAppBar() },
                //bottomBar = { NotesBottomAppBar() },
                floatingActionButton = floatingActionButton,
                isFloatingActionButtonDocked = true,
                floatingActionButtonPosition = FabPosition.End,
                content = content,
            )

            NoteDialog(openDialog)
        }
    }

    @Composable
    fun NotesTopAppBar() {
        TopAppBar(
            title = {

                ConstraintLayout(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(end = 16.dp)
                ) {

                    val (header) = createRefs()
                    Text(
                        text = "Notes",
                        style = Typography.h5,
                        modifier = Modifier.constrainAs(header) {
                            centerTo(parent)
                        }
                    )
                }

            },
            backgroundColor = MaterialTheme.colors.background,
            elevation = 0.dp,
            modifier = Modifier.height(80.dp)
        )
    }

    @Composable
    private fun NotesFAB(function: (() -> Unit)?) {
        FloatingActionButton(
            onClick = {
                function?.invoke()
            }
        ) {
            Icon(Icons.Filled.Add, "")
        }
    }

    @Composable
    private fun NotesBottomAppBar() {
        BottomAppBar(
            content = {},
            elevation = 0.dp
        )
    }

    @OptIn(ExperimentalFoundationApi::class)
    @ExperimentalUnitApi
    @Composable
    fun NoteGrid(notes: List<NotesEntity>) {
        LazyVerticalGrid(
            cells = GridCells.Fixed(2),
            modifier = Modifier.padding(start = 6.dp, end = 6.dp)
        ) {
            items(notes.size) { position ->
                NoteCard(notes[position]) { note ->
                    Toast.makeText(this@NoteListActivity, note.description, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}