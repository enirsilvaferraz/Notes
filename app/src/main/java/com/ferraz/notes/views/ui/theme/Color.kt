package com.ferraz.notes.views.ui.theme

import androidx.compose.ui.graphics.Color

abstract class DefaultColors {
    val primary = Color(0xFFFF0000)                      // Background dos botoes primarios
    val secondary = Color(0xFFFF0000)                    // Background dos botoes secundarios
}

object DarkColors: DefaultColors() {

    val primaryVariant: Color = Color(0xFF43E91E)
    val secondaryVariant: Color = Color(0xFFFFC107)

    val background: Color = Color(0xFF191A1D)           // Background da tela
    val surface: Color = Color(0xFF35363A)              // Background de componentes (sofre variação com relação a elevação)
    val error: Color = Color(0xFFAD0404)

    val onPrimary: Color = Color(0xFFFFFFFF)            // Textos e icones sobre botoes primarios
    val onSecondary: Color = Color(0xFFFFFFFF)          // Textos e icones sobre botoes secundarios
    val onBackground: Color = Color(0xFFFFFFFF)         // Textos e icones sobre o background da tela
    val onSurface: Color = Color(0xFFFFFFFF)            // Textos e icones sobre o background dos componentes
    val onError: Color = Color(0xFFFFFFFF)

}

object LightColors : DefaultColors(){

    val primaryVariant: Color = Color(0xff000000)
    val secondaryVariant: Color = Color(0xffff6434)

    val background: Color = Color(0xFFE1E2E1)
    val surface: Color = Color(0xFFF5F5F6)
    val error: Color = Color(0xFFB00020)

    val onPrimary: Color = Color(0xFFFFFFFF)
    val onSecondary: Color = Color(0xFFFFFFFF)
    val onBackground: Color = Color(0xFF000000)
    val onSurface: Color = Color(0xFF000000)
    val onError: Color = Color(0xFFFFFFFF)
}

/*
<resources>
<color name="primaryColor">0xff212121</color>
<color name="primaryLightColor">0xff484848</color>
<color name="primaryDarkColor">0xff000000</color>

<color name="secondaryColor">0xffdd2c00</color>
<color name="secondaryLightColor">0xffff6434</color>
<color name="secondaryDarkColor">0xffa30000</color>

<color name="primaryTextColor">0xffffffff</color>
<color name="secondaryTextColor">0xffffffff</color>
</resources>
 */