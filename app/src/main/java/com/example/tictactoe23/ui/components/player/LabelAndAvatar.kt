package com.example.tictactoe23.ui.components.player

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.tictactoe23.models.PlayerAI
import com.example.tictactoe23.models.PlayerHuman
import com.example.tictactoe23.models.PlayerModel
import com.example.tictactoe23.ui.components.AvatarIcon
import com.example.tictactoe23.ui.components.general.preview.PreviewColumn

@Composable
fun LabelAndAvatar(
    modifier: Modifier = Modifier,
    playerModel: PlayerModel,
    size: Int
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy((size/3).dp),
        modifier = modifier
    ) {
        PlayerLabelIcon(
            fontSize = size,
            text = playerModel.label.name
        )
        AvatarIcon(
            size = size,
            model = playerModel.avatar
        )
    }
}

@Preview
@Composable
fun LabelAndAvatarPreview() {
    val size = 48
    PreviewColumn {
        LabelAndAvatar(
            playerModel = PlayerHuman,
            size = size
        )
        LabelAndAvatar(
            playerModel = PlayerAI,
            size = size
        )
    }
}