package com.example.tictactoe23.ui.components.general

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

@Composable
fun Icon(
    modifier: Modifier = Modifier,
    size: Int,
    @DrawableRes
    iconDrawable: Int,
    contentDescription: String = "an icon"
) {
    Image(
        painter = painterResource(id = iconDrawable),
        contentDescription = contentDescription,
        contentScale = ContentScale.Fit,
        modifier = modifier
            .size(size.dp)
    )
}