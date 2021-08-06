package com.ferraz.notes.views

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.material.BottomAppBar
import androidx.compose.material.Card
import androidx.compose.material.FabPosition
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.ferraz.notes.database.NotesEntity
import com.ferraz.notes.views.ui.theme.DarkColors
import com.ferraz.notes.views.ui.theme.LightColors
import com.ferraz.notes.views.ui.theme.NotesTheme
import com.ferraz.notes.views.ui.theme.Shapes
import com.ferraz.notes.views.ui.theme.Typography
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NoteListActivity : AppCompatActivity() {

    //private val notesVM: NoteListViewModel by viewModels()

    @ExperimentalUnitApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            val systemUiController = rememberSystemUiController()
            val inDarkMode = isSystemInDarkTheme()

            SideEffect {
                systemUiController.setSystemBarsColor(
                    color = if (inDarkMode) DarkColors.background else LightColors.background,
                    darkIcons = !inDarkMode
                )
            }

            /*
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
             */
            NotesAppScreen()
        }
    }

    @ExperimentalUnitApi
    @Preview(showBackground = true, name = "App Main Screen")
    @Composable
    fun NotesAppScreen(noteView: List<NotesEntity> = MockHelper.items) {
        NotesTheme {
            Scaffold(
                topBar = { NotesTopAppBar() },
                bottomBar = { NotesBottomAppBar() },
                floatingActionButton = { NotesFAB() },
                isFloatingActionButtonDocked = true,
                floatingActionButtonPosition = FabPosition.End,
                content = {
                    NoteGrid(noteView)
                },
            )
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
            //backgroundColor = MaterialTheme.colors.background,
            elevation = 0.dp,
            modifier = Modifier.height(80.dp)
        )
    }

    @Composable
    private fun NotesFAB() {
        FloatingActionButton(
            onClick = {}
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

    @ExperimentalUnitApi
    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    fun NoteGrid(notes: List<NotesEntity>) {
        LazyVerticalGrid(
            cells = GridCells.Fixed(2),
            modifier = Modifier.padding(start = 6.dp, end = 6.dp)
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