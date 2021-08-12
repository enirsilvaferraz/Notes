package com.ferraz.notes.views.ui.components

import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.ferraz.notes.views.ui.theme.Typography


@ExperimentalUnitApi
@Preview(showBackground = true)
@Composable
fun LoadingScreen() = ConstraintLayout(modifier = Modifier.fillMaxSize()) {

    val (container, progress) = createRefs()

    CircularProgressIndicator(
        color = MaterialTheme.colors.secondary,
        modifier = Modifier
            .width(200.dp)
            .height(200.dp)
            .constrainAs(progress) {
                centerTo(parent)
            })

    ConstraintLayout(modifier = Modifier.constrainAs(container) {
        top.linkTo(progress.bottom)
        centerHorizontallyTo(parent)
        bottom.linkTo(parent.bottom)
    }) {

        val (title, description) = createRefs()

        Text(
            text = "Buscando notas",
            textAlign = TextAlign.Center,
            style = Typography.subtitle2,
            modifier = Modifier.constrainAs(title) {
                top.linkTo(parent.top)
                centerHorizontallyTo(parent)
            })

        Text(
            text = "As notas cadastradas aparecerão aqui assim que a consulta terminar",
            textAlign = TextAlign.Center,
            style = Typography.caption,
            modifier = Modifier
                .width(250.dp)
                .height(IntrinsicSize.Min)
                .constrainAs(description) {
                    top.linkTo(title.bottom, margin = 8.dp)
                    centerHorizontallyTo(title)
                }
        )
    }
}