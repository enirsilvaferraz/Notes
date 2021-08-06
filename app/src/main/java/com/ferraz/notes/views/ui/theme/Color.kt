package com.ferraz.notes.views.ui.theme

import androidx.compose.ui.graphics.Color

object DefaultColors {
    val primary = Color(0xff212121)
    val secondary = Color(0xffdd2c00)
}

object DarkColors {

    val primary: Color = DefaultColors.primary
    val primaryVariant: Color = Color(0xff484848)
    val secondary: Color = DefaultColors.secondary
    val secondaryVariant: Color = Color(0xffa30000)

    val background: Color = Color(0xFF121212)
    val surface: Color = Color(0xFF121212)
    val error: Color = Color(0xFFCF6679)

    val onPrimary: Color = Color(0xFF000000)
    val onSecondary: Color = Color(0xFFFFFFFF)
    val onBackground: Color = Color(0xFFFFFFFF)
    val onSurface: Color = Color(0xFFFFFFFF)
    val onError: Color = Color(0xFF000000)
}

object LightColors {

    val primary: Color = DefaultColors.primary
    val primaryVariant: Color = Color(0xff000000)
    val secondary: Color = DefaultColors.secondary
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