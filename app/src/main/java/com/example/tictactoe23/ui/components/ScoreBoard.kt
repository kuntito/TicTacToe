package com.example.tictactoe23.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.tictactoe23.ui.components.general.Label
import com.example.tictactoe23.ui.components.general.preview.PreviewColumn


@Composable
fun ScoreBoard(
    modifier: Modifier = Modifier,
    score1: Int,
    score2: Int,
    size: Int
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy((size * 0.083f).dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        Label(
            text = score1.toString(),
            fontSize = size
        )
        Label(
            text = ":",
            fontSize = size
        )
        Label(
            text = score2.toString(),
            fontSize = size
        )
    }
}

@Preview
@Composable
fun ScoreBoardPreview() {
    PreviewColumn {
        ScoreBoard(
            score1 = 0,
            score2 = 3,
            size = 48
        )
    }
}