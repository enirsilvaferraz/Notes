package com.ferraz.notes.views.ui.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.ferraz.notes.database.NotesEntity
import com.ferraz.notes.views.MockHelper
import com.ferraz.notes.views.ui.theme.Shapes
import com.ferraz.notes.views.ui.theme.Typography

@ExperimentalFoundationApi
@Composable
fun NoteCard(
    note: NotesEntity,
    onClick: ((note: NotesEntity) -> Unit)? = null,
    onLongClick: ((note: NotesEntity) -> Unit)? = null
) {

    Card(
        shape = Shapes.medium,
        elevation = 4.dp,
        modifier = Modifier
            .padding(4.dp)
            .height(150.dp)
            .combinedClickable(
                onClick = {
                    onClick?.invoke(note)
                },
                onLongClick = {
                    onLongClick?.invoke(note)
                }
            )
    ) {

        ConstraintLayout(
            modifier = Modifier.fillMaxSize()
        ) {

            val (header, subtitle) = createRefs()

            if (!note.title.isNullOrBlank())
                Text(
                    text = note.title.orEmpty(),
                    style = Typography.h6,
                    overflow = TextOverflow.Ellipsis,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    modifier = Modifier.constrainAs(header) {
                            top.linkTo(parent.top, 16.dp)
                            start.linkTo(parent.start, 16.dp)
                            end.linkTo(parent.end, 16.dp)
                            width = Dimension.fillToConstraints
                            height = Dimension.wrapContent

                        }
                )

            if (!note.description.isNullOrBlank())
                Text(
                    text = note.description.orEmpty(),
                    style = Typography.body1,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = if (!note.title.isNullOrBlank()) 5 else 7,
                    textAlign = TextAlign.Justify,
                    modifier = Modifier.constrainAs(subtitle) {

                            if (!note.title.isNullOrBlank()) top.linkTo(header.bottom, 6.dp)
                            else top.linkTo(parent.top, 16.dp)

                            start.linkTo(parent.start, 16.dp)
                            end.linkTo(parent.end, 16.dp)
                            bottom.linkTo(parent.bottom, 8.dp)
                            width = Dimension.fillToConstraints
                            height = Dimension.fillToConstraints
                        }
                )
        }
    }
}

@ExperimentalFoundationApi
@Composable
@Preview
fun PreviewNoteCard() {
    Column {
        NoteCard(MockHelper.items[0])
        NoteCard(MockHelper.items[1])
        NoteCard(MockHelper.items[2])
        NoteCard(MockHelper.items[3])
        NoteCard(MockHelper.items[4])
    }
}