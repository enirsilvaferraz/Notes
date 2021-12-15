package com.ferraz.notes.views.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.ferraz.notes.views.ui.theme.Typography

@ExperimentalAnimationApi
@Composable
fun NotesTopAppBar(title: String, loadingValueState: Boolean?) {

    val content = @Composable {

        ConstraintLayout(
            modifier = Modifier
                .fillMaxSize()
                .padding(end = 16.dp)
        ) {

            val (header, progress) = createRefs()

            Text(
                text = title,
                style = Typography.h5,
                modifier = Modifier.constrainAs(header) {
                    centerTo(parent)
                }
            )

            AnimatedVisibility(
                visible = loadingValueState ?: false,
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

@ExperimentalAnimationApi
@Preview
@Composable
fun NotesTopAppBarPreview() {
    NotesTopAppBar("Notes", true)
}