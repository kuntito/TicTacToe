package com.example.tictactoe23.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.tictactoe23.models.PlayerAI
import com.example.tictactoe23.models.PlayerHuman
import com.example.tictactoe23.models.PlayerModel
import com.example.tictactoe23.ui.components.player.LeftPlayerDesc
import com.example.tictactoe23.ui.components.player.RightPlayerDesc
import com.example.tictactoe23.ui.components.general.preview.PreviewColumn

@Composable
fun GameScoreRow(
    modifier: Modifier = Modifier,
    iconSize: Int,
    rowWidth: Int,
    player1: PlayerModel,
    player2: PlayerModel,
    currentPlayer: PlayerModel,
) {

    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.width(rowWidth.dp)
    ) {
        LeftPlayerDesc(
            size = iconSize,
            player = player1,
            currentPlayer = currentPlayer
        )
        ScoreBoard(
            score1 = player1.score,
            score2 = player2.score,
            size = iconSize
        )
        RightPlayerDesc(
            size = iconSize,
            player = player2,
            currentPlayer = currentPlayer
        )
    }
}

@Preview
@Composable
fun GameScoreRowPreview() {
    PreviewColumn {
        GameScoreRow(
            iconSize = 24,
            rowWidth = 360,
            player1 = PlayerAI,
            player2 = PlayerHuman,
            currentPlayer = PlayerHuman
        )
    }
}