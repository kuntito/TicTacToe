package com.example.tictactoe23.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.tictactoe23.R
import com.example.tictactoe23.ui.components.general.Icon
import com.example.tictactoe23.ui.components.general.preview.PreviewColumn
import com.example.tictactoe23.ui.theme.color200
import com.example.tictactoe23.ui.theme.color300
import kotlinx.coroutines.delay

enum class RestartButtonSkin {
    OnDialog,
    OnScreen
}

@Composable
fun GameRestartButton(
    modifier: Modifier = Modifier,
    size: Int,
    onClick: () -> Unit,
    skin: RestartButtonSkin,
    contentDescription: String = "game restart button",
) {

    var isButtonClicked by remember { mutableStateOf(false) }
    LaunchedEffect(isButtonClicked) {
        delay(150) // Adjust the delay duration as needed
        isButtonClicked = false
    }

    var surfaceColor: Color = Color.Unspecified
    var rippleColor: Color = Color.Unspecified
    var iconDrawable = 0
    if (skin == RestartButtonSkin.OnDialog) {
        surfaceColor = color200
        rippleColor = Color.Black
        iconDrawable = R.drawable.ic_restart_color300
    } else if (skin == RestartButtonSkin.OnScreen) {
        surfaceColor = color300
        rippleColor = Color.White
        iconDrawable = R.drawable.ic_restart_color200
    }

    Surface(
        shape = CircleShape,
        color = surfaceColor,
        modifier = modifier
            .shadow(
                elevation = (size/3).dp,
                shape = CircleShape,
                spotColor = Color.Black,
                ambientColor = Color.Black
            )
            .alpha(if (isButtonClicked) 0.7f else 1f)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = rememberRipple(
                    color = rippleColor
                )
            ) {
                isButtonClicked = true
                onClick()
            }
    ) {
        Icon(
            iconDrawable = iconDrawable,
            size = size,
            contentDescription = contentDescription,
            modifier = Modifier
                .padding(4.dp)
        )
    }
}

@Preview
@Composable
fun GameRestartButtonPreview() {
    val iconSize = 48
    PreviewColumn {
        GameRestartButton(
            size = iconSize,
            skin = RestartButtonSkin.OnDialog,
            onClick = {}
        )
        GameRestartButton(
            size = iconSize,
            skin = RestartButtonSkin.OnScreen,
            onClick = {}
        )
    }
}