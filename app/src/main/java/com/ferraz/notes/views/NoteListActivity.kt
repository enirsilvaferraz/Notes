package com.ferraz.notes.views

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.material.BottomAppBar
import androidx.compose.material.Card
import androidx.compose.material.FabPosition
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ferraz.notes.database.NotesEntity
import com.ferraz.notes.views.ui.theme.NotesTheme
import com.ferraz.notes.views.ui.theme.Shapes
import com.ferraz.notes.views.ui.theme.Typography

class NoteListActivity : AppCompatActivity() {

    @ExperimentalUnitApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            val notesVM: NoteListViewModel = viewModel()
            notesVM.notes.observeAsState().value?.let { state ->

                when (state) {

                    NoteListViewModel.NotesState.Loading -> {
                        // do nothing
                    }

                    NoteListViewModel.NotesState.Empty -> {
                        EmptyScreen()
                    }

                    is NoteListViewModel.NotesState.Failure -> {
                        // do nothing
                    }

                    is NoteListViewModel.NotesState.Success -> {
                        AppPreview(state.data)
                    }
                }
            }
        }
    }

    @ExperimentalUnitApi
    @Preview(showBackground = true)
    @Composable
    fun AppPreview(noteView: List<NotesEntity> = MockHelper.items) {
        NotesTheme {
            Surface(color = MaterialTheme.colors.background) {
                Scaffold(
                    topBar = { NotesTopAppBar() },
                    content = { NoteGrid(noteView) },
                    bottomBar = { NotesBottomAppBar() },
                    floatingActionButton = { NotesFAB() },
                    isFloatingActionButtonDocked = true,
                    floatingActionButtonPosition = FabPosition.End,
                )
            }
        }
    }

    @Composable
    fun NotesTopAppBar() = TopAppBar(
        title = { Text(text = "Notes") },
        backgroundColor = MaterialTheme.colors.background,
        elevation = 0.dp
    )

    @Composable
    private fun NotesFAB() {
        FloatingActionButton(
            onClick = {},
            backgroundColor = Color(0xFFFF8C00)
        ) {
            Icon(Icons.Filled.Add, "")
        }
    }

    @Composable
    private fun NotesBottomAppBar() {
        BottomAppBar(content = {}, backgroundColor = MaterialTheme.colors.background, elevation = 4.dp)
    }

    @ExperimentalUnitApi
    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    fun NoteGrid(notes: List<NotesEntity>) {
        LazyVerticalGrid(
            cells = GridCells.Fixed(2),
            modifier = Modifier.padding(start = 12.dp, end = 12.dp)
        ) {
            items(notes.size) { position ->
                NoteCard(notes[position])
            }
        }
    }


    @ExperimentalUnitApi
    @Composable
    fun NoteCard(note: NotesEntity) {

        Card(
            shape = Shapes.medium,
            elevation = 4.dp,
            modifier = Modifier
                .padding(4.dp)
                .height(100.dp)
                .clickable {
                    // do something
                }
        ) {

            ConstraintLayout {

                val (header) = createRefs()

                Text(
                    text = note.description,
                    style = Typography.body2,
                    modifier = Modifier.constrainAs(header) {
                        top.linkTo(parent.top, margin = 8.dp)
                        start.linkTo(parent.start, margin = 8.dp)
                        end.linkTo(parent.end, margin = 8.dp)
                        bottom.linkTo(parent.bottom, margin = 8.dp)
                        width = Dimension.fillToConstraints
                        height = Dimension.fillToConstraints
                    }
                )
            }
        }
    }
}