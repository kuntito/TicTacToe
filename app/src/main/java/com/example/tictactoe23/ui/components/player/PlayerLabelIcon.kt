package com.example.tictactoe23.ui.components.player

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.tictactoe23.models.PlayerAI
import com.example.tictactoe23.models.PlayerHuman
import com.example.tictactoe23.ui.components.general.Label
import com.example.tictactoe23.ui.components.general.preview.PreviewColumn

@Composable
fun PlayerLabelIcon(
    modifier: Modifier = Modifier,
    fontSize: Int,
    text: String,
) {
    Label(
        text = text,
        fontSize = fontSize,
        modifier = modifier
    )
}

@Preview
@Composable
fun PlayerLabelPreview() {
    val size = 48
    PreviewColumn {
        PlayerLabelIcon(
            fontSize = size,
            text = PlayerHuman.label.name
        )
        PlayerLabelIcon(
            fontSize = size,
            text = PlayerAI.label.name
        )
    }
}