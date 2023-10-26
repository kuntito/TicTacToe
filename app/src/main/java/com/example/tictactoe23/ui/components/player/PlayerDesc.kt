package com.example.tictactoe23.ui.components.player

import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.tictactoe23.models.PlayerAI
import com.example.tictactoe23.models.PlayerHuman
import com.example.tictactoe23.models.PlayerModel
import com.example.tictactoe23.ui.components.general.preview.PreviewColumn

enum class IconLR {
    L,
    R
}

@Composable
fun PlayerDesc(
    modifier: Modifier = Modifier,
    size: Int,
    player: PlayerModel,
    currentPlayer: PlayerModel,
    iconLR: IconLR
) {
    val padding = (size * 0.67)
    val isCurrentPlayer = player == currentPlayer
    val alpha = if (isCurrentPlayer) 1f else 0.6f
    val scaleValue = if (isCurrentPlayer) 1f else 0.6f
    val scale = remember { Animatable(scaleValue) }

    LaunchedEffect(currentPlayer) {
        scale.animateTo(scaleValue)
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(padding.dp),
        modifier = modifier
            .alpha(alpha)
            .scale(scale.value)
    ) {
        if (iconLR == IconLR.L) {
            PlayerIcon(
                model = player,
                size = size,
            )
            LabelAndAvatar(
                playerModel = player,
                size = size,
            )
        } else if (iconLR == IconLR.R) {
            LabelAndAvatar(
                playerModel = player,
                size = size,
            )
            PlayerIcon(
                model = player,
                size = size,
            )
        }
    }
}

@Composable
fun LeftPlayerDesc(
    modifier: Modifier = Modifier,
    size: Int,
    player: PlayerModel,
    currentPlayer: PlayerModel,
    iconLR: IconLR = IconLR.L
) {
    PlayerDesc(
        size = size,
        player = player,
        currentPlayer = currentPlayer,
        iconLR = iconLR,
        modifier = modifier
    )
}

@Composable
fun RightPlayerDesc(
    modifier: Modifier = Modifier,
    size: Int,
    player: PlayerModel,
    currentPlayer: PlayerModel,
    iconLR: IconLR = IconLR.L
) {
    PlayerDesc(
        size = size,
        player = player,
        currentPlayer = currentPlayer,
        iconLR = iconLR,
        modifier = modifier
    )
}

@Preview
@Composable
fun PlayerDescPreview() {
    val size = 48
    val currentPlayer = PlayerAI
    PreviewColumn {
        LeftPlayerDesc(
            size = size,
            player = PlayerHuman,
            currentPlayer = currentPlayer
        )
        RightPlayerDesc(
            size = size,
            player = PlayerAI,
            currentPlayer = currentPlayer
        )
    }
}