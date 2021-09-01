package com.ferraz.notes.views.ui.features.notes.register

import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun NoteDialog(
    openDialogState: MutableState<Boolean> = mutableStateOf(true),
    vm: NoteRegisterViewModel = viewModel()
) {

    if (openDialogState.value) {

        val textState = remember { mutableStateOf(TextFieldValue()) }

        AlertDialog(
            onDismissRequest = {
                openDialogState.value = false
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
                    openDialogState.value = false
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
