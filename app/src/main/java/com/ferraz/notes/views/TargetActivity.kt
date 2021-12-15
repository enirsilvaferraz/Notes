package com.ferraz.notes.views

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import com.ferraz.notes.views.ui.components.NotesTopAppBar
import com.ferraz.notes.views.ui.theme.NotesTheme
import com.ferraz.notes.views.ui.theme.StatusBar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TargetActivity : ComponentActivity() {

    @ExperimentalAnimationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            val state = remember { mutableStateOf(ScaffoldDataState("Targets", false)) }

            NotesTheme {
                StatusBar()
                NotesScaffold(state)
            }
        }
    }

    data class ScaffoldDataState(val title: String, val loading: Boolean)

    @ExperimentalAnimationApi
    @Composable
    fun NotesScaffold(state: State<ScaffoldDataState>) {

        Scaffold(topBar = { NotesTopAppBar(state.value.title, state.value.loading) }) {

        }

    }

    @Preview(showBackground = true, name = "Target List Screen - Success")
    @Composable
    fun TargetActivityPreview() {
        NotesTheme {

        }
    }
}