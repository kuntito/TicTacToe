package com.tito.tictactoe.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.material.ripple.RippleAlpha
import androidx.compose.material.ripple.RippleTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorPalette = darkColors(
)

private val LightColorPalette = lightColors(
    primary = LightBlue200,
    primaryVariant = LightBlue300,
    secondary = LightGreen200,
    secondaryVariant= LightGreen300,
    background = LightGreen100,
    surface = Blue200,
    onSurface = Color.White
    /* Other default colors to override
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black,
    */
)

@Composable
fun TicTacToeTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
/*
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }
*/
    val colors = LightColorPalette

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}

object RippleCustomTheme: RippleTheme {

    //Your custom implementation...
    @Composable
    override fun defaultColor() =
        RippleTheme.defaultRippleColor(
            Color.Red,
            lightTheme = true
        )

    @Composable
    override fun rippleAlpha(): RippleAlpha =
        RippleTheme.defaultRippleAlpha(
            Color.Black,
            lightTheme = true
        )
}