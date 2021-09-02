package com.ferraz.notes.views.ui.features.notes.register

import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ferraz.notes.views.NoteListViewModel

@Composable
fun NoteDialog(
    openDialogState: State<NoteListViewModel.Actions?>,
    vm: NoteRegisterViewModel = viewModel(),
    onDismissRequest: () -> Unit
) {

    if (openDialogState.value is NoteListViewModel.Actions.OpenDialog) {

        val textState = remember { mutableStateOf(TextFieldValue()) }

        AlertDialog(
            onDismissRequest = {
                onDismissRequest()
            },
            title = {
                Text("Nova Nota")
            },
            text = {
                Content(textState)
            },
            confirmButton = {
                ConfirmButton(textState) {
                    vm.onConfirm(textState.value.text)
                    onDismissRequest()
                }
            }
        )
    }
}

@Preview
@Composable
private fun Content(
    textState: MutableState<TextFieldValue> = mutableStateOf(TextFieldValue("Texto"))
) {

    TextField(
        value = textState.value,
        label = { Text("Enter text") },

        onValueChange = {
            textState.value = it
        }
    )
}


@Preview
@Composable
private fun ConfirmButton(
    textState: MutableState<TextFieldValue> = mutableStateOf(TextFieldValue("")),
    onClick: (String) -> Unit = {},
) {
    TextButton(
        onClick = {
            onClick(textState.value.text)
        }
    ) {
        Text("Confirmar")
    }
}
