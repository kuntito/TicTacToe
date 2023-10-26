package com.example.tictactoe23.ui.components.player

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.tictactoe23.models.PlayerAI
import com.example.tictactoe23.models.PlayerHuman
import com.example.tictactoe23.models.PlayerModel
import com.example.tictactoe23.ui.components.general.Icon
import com.example.tictactoe23.ui.components.general.preview.PreviewColumn

@Composable
fun PlayerIcon(
    modifier: Modifier = Modifier,
    size: Int,
    model: PlayerModel
) {
    Icon(
        size = size,
        iconDrawable = model.iconDrawable,
        contentDescription = "player ${model.label.name}" ,
        modifier = modifier
    )
}

@Preview
@Composable
fun PlayerIconPreview() {
    val size = 48
    PreviewColumn {
        PlayerIcon(
            size = size,
            model = PlayerHuman
        )
        PlayerIcon(
            size = size,
            model = PlayerAI
        )
    }
}