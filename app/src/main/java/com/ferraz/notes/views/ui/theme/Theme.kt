package com.ferraz.notes.views.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import com.google.accompanist.systemuicontroller.rememberSystemUiController

private val DarkColorPalette = with(DarkColors) {
    darkColors(
        primary = primary,
        primaryVariant = primaryVariant,
        secondary = secondary,
        secondaryVariant = secondaryVariant,
        background = background,
        surface = surface,
        error = error,
        onPrimary = onPrimary,
        onSecondary = onSecondary,
        onBackground = onBackground,
        onSurface = onSurface,
        onError = onError
    )
}

private val LightColorPalette = with(LightColors) {
    lightColors(
        primary = primary,
        //primaryVariant = primaryVariant,
        secondary = secondary,
        //secondaryVariant = secondaryVariant,
        //background = background,
        //surface = surface,
        //error = error,
        //onPrimary = onPrimary,
        //onSecondary = onSecondary,
        //onBackground = onBackground,
        //onSurface = onSurface,
        //onError = onError
    )
}

@Composable
fun NotesTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable() () -> Unit) {
    MaterialTheme(
        colors = if (darkTheme) DarkColorPalette else LightColorPalette,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}

@Composable
fun StatusBar() {
    val systemUiController = rememberSystemUiController()
    val inDarkMode = isSystemInDarkTheme()

    SideEffect {
        systemUiController.setSystemBarsColor(
            color = if (inDarkMode) DarkColors.background else LightColors.background,
            darkIcons = !inDarkMode
        )
    }
}