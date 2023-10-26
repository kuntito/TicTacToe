package com.example.tictactoe23.ui.components.general

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import com.example.tictactoe23.ui.theme.customTextStyle

@Composable
fun Label(
    modifier: Modifier = Modifier,
    text: String,
    fontSize: Int,
) {
    Text(
        text = text,
        maxLines = 1,
        style = customTextStyle.copy(fontSize = fontSize.sp),
        modifier = modifier
    )
}