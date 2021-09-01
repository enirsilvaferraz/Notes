package com.ferraz.notes.views.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.ferraz.notes.database.NotesEntity
import com.ferraz.notes.views.MockHelper
import com.ferraz.notes.views.ui.theme.Shapes
import com.ferraz.notes.views.ui.theme.Typography

@ExperimentalUnitApi
@Composable
@Preview
fun NoteCard(note: NotesEntity = MockHelper.items[0], onClick: ((note: NotesEntity) -> Unit)? = null) {

    Card(
        shape = Shapes.large,
        elevation = 4.dp,
        modifier = Modifier
            .padding(4.dp)
            .height(110.dp)
            .clickable {
                onClick?.invoke(note)
            }
    ) {

        ConstraintLayout {

            val (header) = createRefs()

            Text(
                text = note.description,
                style = Typography.body1,
                overflow = TextOverflow.Ellipsis,
                maxLines = 5,
                modifier = Modifier
                    .padding(12.dp)
                    .fillMaxSize()
                    .constrainAs(header) {
                        centerTo(parent)
                    }
            )
        }
    }
}