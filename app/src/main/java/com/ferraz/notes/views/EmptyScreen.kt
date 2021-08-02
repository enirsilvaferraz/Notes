package com.ferraz.notes.views

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.ferraz.notes.R
import com.ferraz.notes.views.ui.theme.Typography

@ExperimentalUnitApi
@Preview(showBackground = true)
@Composable
fun EmptyScreen() = ConstraintLayout(modifier = Modifier.fillMaxSize()) {

    val (container) = createRefs()

    ConstraintLayout(modifier = Modifier.constrainAs(container) {
        centerTo(parent)
    }) {

        val (image, title, description) = createRefs()

        Image(
            painter = painterResource(id = R.drawable.ic_baseline_view_list_24),
            contentDescription = "Lista Vazia",
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .width(200.dp)
                .height(200.dp)
                .padding(end = 8.dp)
                .constrainAs(image) {
                    top.linkTo(parent.top)
                    centerHorizontallyTo(parent)
                })

        Text(
            text = "Nenhuma nota encontrada",
            textAlign = TextAlign.Center,
            style = Typography.subtitle2,
            modifier = Modifier.constrainAs(title) {
                top.linkTo(image.bottom)
                centerHorizontallyTo(image)
            })

        Text(
            text = "As notas cadastradas aparecerão aqui. Adicione uma nota utilizando o botão abaixo.",
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