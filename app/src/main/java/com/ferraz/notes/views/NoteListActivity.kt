package com.ferraz.notes.views

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
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
import androidx.compose.runtime.State
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
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

    private val vm: NoteListViewModel by viewModels()

    @ExperimentalUnitApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            NotesAppScreen()
        }

        lifecycle.addObserver(vm)
    }

    @ExperimentalUnitApi
    @Preview(showBackground = true, name = "App Main Screen")
    @Composable
    fun NotesAppScreen(state: State<NoteListViewModel.NotesState?> = mutableStateOf(Success(data = MockHelper.items))) {

        NotesTheme {

            StatusBar()

            Scaffold(
                topBar = { NotesTopAppBar() },
                floatingActionButton = { NotesFAB(state) },
                isFloatingActionButtonDocked = true,
                floatingActionButtonPosition = FabPosition.End,
                content = {
                    when (val value = vm.notes.observeAsState().value) {
                        is Loading -> LoadingScreen()
                        is Empty -> EmptyScreen()
                        is Success -> NoteGrid(value)
                        is Failure -> {/* do nothing */ }
                    }
                }
            )

            NoteDialog(vm.actions.observeAsState()) { vm.onDismissDialog() }
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
    private fun NotesFAB(state: State<NoteListViewModel.NotesState?> = mutableStateOf(Empty)) {
        if (state.value is Empty || state.value is Success) {
            FloatingActionButton(
                onClick = { vm.onCardClick() }
            ) {
                Icon(Icons.Filled.Add, "")
            }
        }
    }

    @OptIn(ExperimentalFoundationApi::class)
    @ExperimentalUnitApi
    @Composable
    fun NoteGrid(state: Success = Success(data = MockHelper.items)) {

            LazyVerticalGrid(
                cells = GridCells.Fixed(2),
                modifier = Modifier.padding(start = 6.dp, end = 6.dp)
            ) {
                items(state.data.size) { position ->
                    NoteCard(
                        state.data[position],
                        { note -> vm.onCardClick(note) },
                        { note -> vm.onCardLongClick(note) },
                    )
                }
            }
    }
}