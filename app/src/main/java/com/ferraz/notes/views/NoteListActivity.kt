package com.ferraz.notes.views

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.material.AlertDialog
import androidx.compose.material.FabPosition
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TextField
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.ferraz.notes.views.NoteListViewModel.NotesState.*
import com.ferraz.notes.views.ui.components.EmptyScreen
import com.ferraz.notes.views.ui.components.NoteCard
import com.ferraz.notes.views.ui.theme.NotesTheme
import com.ferraz.notes.views.ui.theme.StatusBar
import com.ferraz.notes.views.ui.theme.Typography
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class NoteListActivity : AppCompatActivity() {

    private val vm: NoteListViewModel by viewModels()

    @ExperimentalAnimationApi
    @ExperimentalUnitApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            NotesTheme {
                StatusBar()
                NotesListScreen(vm.notes.observeAsState(), vm.loading.observeAsState())
                NoteDialog(vm.actions.observeAsState())
            }
        }

        lifecycle.addObserver(vm)
    }

    @ExperimentalAnimationApi
    @ExperimentalUnitApi
    @Composable
    fun NotesListScreen(state: State<NoteListViewModel.NotesState?>, loading: State<Boolean?>) {

        val snackbarHostState = remember { SnackbarHostState() }
        val coroutineState = rememberCoroutineScope()

        Scaffold(
            topBar = { NotesTopAppBar(loading.value) },
            floatingActionButton = { NotesFAB() },
            floatingActionButtonPosition = FabPosition.End,
            scaffoldState = rememberScaffoldState(snackbarHostState = snackbarHostState),
            content = {
                when (val valueState = state.value) {
                    is Empty -> EmptyScreen()
                    is Success -> NoteGrid(valueState)
                    is Failure -> {
                        coroutineState.launch {
                            snackbarHostState.showSnackbar(valueState.message)
                        }
                    }
                }
            }
        )
    }

    @ExperimentalAnimationApi
    @Composable
    fun NotesTopAppBar(valueState: Boolean?) {

        val content = @Composable {

            ConstraintLayout(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(end = 16.dp)
            ) {

                val (header, progress) = createRefs()

                Text(
                    text = "Notes",
                    style = Typography.h5,
                    modifier = Modifier.constrainAs(header) {
                        centerTo(parent)
                    }
                )

                AnimatedVisibility(
                    visible = valueState ?: false,
                    enter = expandVertically(),
                    exit = shrinkVertically(),
                    modifier = Modifier.constrainAs(progress) {
                        centerAround(parent.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        width = Dimension.fillToConstraints
                    }
                ) {
                    LinearProgressIndicator()
                }
            }
        }

        TopAppBar(
            title = content,
            backgroundColor = MaterialTheme.colors.background,
            elevation = 0.dp,
            modifier = Modifier.height(80.dp)
        )
    }

    @Composable
    private fun NotesFAB() {

        FloatingActionButton(
            onClick = { vm.onCardClick() }
        ) {
            Icon(Icons.Filled.Add, "")
        }
    }

    @OptIn(ExperimentalFoundationApi::class)
    @ExperimentalUnitApi
    @Composable
    fun NoteGrid(
        valueState: Success = Success(data = MockHelper.items)
    ) {

        LazyVerticalGrid(
            cells = GridCells.Fixed(2),
            modifier = Modifier.padding(top = 16.dp, start = 6.dp, end = 6.dp)
        ) {
            items(valueState.data.size) { position ->
                NoteCard(
                    valueState.data[position],
                    { note -> vm.onCardClick(note) },
                    { note -> vm.onCardLongClick(note) },
                )
            }
        }
    }

    @Composable
    fun NoteDialog(
        openDialogState: State<NoteListViewModel.Actions?>
    ) {

        if (openDialogState.value is NoteListViewModel.Actions.OpenDialog) {

            val titleState = remember { mutableStateOf(TextFieldValue()) }
            val descriptionState = remember { mutableStateOf(TextFieldValue()) }

            AlertDialog(
                onDismissRequest = {
                    vm.onDismissDialog()
                },
                title = {
                    Text("Nova Nota")
                },
                text = {
                    DialogContent(titleState, descriptionState)
                },
                confirmButton = {

                    TextButton(
                        onClick = {
                            vm.onConfirm(titleState.value.text, descriptionState.value.text)
                            vm.onDismissDialog()
                        }
                    ) {
                        Text("Confirmar")
                    }

                }
            )
        }
    }

    @Composable
    private fun DialogContent(
        titleState: MutableState<TextFieldValue>,
        descriptionState: MutableState<TextFieldValue>
    ) {

        ConstraintLayout {

            val (empty, title, description) = createRefs()

            Text(
                text = "",
                modifier = Modifier.constrainAs(empty) {
                    top.linkTo(parent.top)
                    centerHorizontallyTo(parent)
                },
            )

            TextField(
                value = titleState.value,
                label = { Text("Titulo") },
                modifier = Modifier.constrainAs(title) {
                    top.linkTo(empty.top, 8.dp)
                    centerHorizontallyTo(parent)
                },
                onValueChange = {
                    titleState.value = it
                }
            )

            TextField(
                value = descriptionState.value,
                label = { Text("Descrição") },
                modifier = Modifier.constrainAs(description) {
                    top.linkTo(title.bottom, 16.dp)
                    centerHorizontallyTo(parent)
                },
                onValueChange = {
                    descriptionState.value = it
                }
            )
        }
    }

    /**
     * APP PREVIEW
     */

    @ExperimentalAnimationApi
    @ExperimentalUnitApi
    @Preview(showBackground = true, name = "Notes List Screen - Success")
    @Composable
    fun NotesListPreviewSuccess(
        listState: State<NoteListViewModel.NotesState?> = mutableStateOf(Success(data = MockHelper.items)),
        loadingState: State<Boolean?> = mutableStateOf(false)
    ) {
        NotesTheme { NotesListScreen(listState, loadingState) }
    }

    @ExperimentalAnimationApi
    @ExperimentalUnitApi
    @Preview(showBackground = true, name = "Notes List Screen - Empty")
    @Composable
    fun NotesListPreviewEmpty(
        listState: State<NoteListViewModel.NotesState?> = mutableStateOf(Empty()),
        loadingState: State<Boolean?> = mutableStateOf(true)
    ) {
        NotesTheme { NotesListScreen(listState, loadingState) }
    }

    @Composable
    @Preview
    private fun DialogPreview(
        titleState: MutableState<TextFieldValue> = mutableStateOf(TextFieldValue("Texto")),
        descriptionState: MutableState<TextFieldValue> = mutableStateOf(TextFieldValue("Texto"))
    ) {
        DialogContent(titleState, descriptionState)
    }
}