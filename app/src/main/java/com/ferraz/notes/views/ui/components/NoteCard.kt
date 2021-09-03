package com.ferraz.notes.views.ui.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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
            .height(148.dp)
            .width(344.dp)
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
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 16.dp, top = 16.dp, end = 16.dp, bottom = 8.dp)
        ) {

            val (header, subtitle) = createRefs()

            if (!note.title.isNullOrBlank())
                Text(
                    text = note.title.orEmpty(),
                    style = Typography.h6,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                    modifier = Modifier
                        .fillMaxWidth()
                        .constrainAs(header) {
                            top.linkTo(parent.top)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        }
                )

            if (!note.description.isNullOrBlank())
                Text(
                    text = note.description.orEmpty(),
                    style = Typography.body1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .constrainAs(subtitle) {
                            top.linkTo(
                                if (!note.title.isNullOrBlank()) header.bottom else parent.top
                            )
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                            bottom.linkTo(parent.bottom)
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
    }
}