package com.example.tictactoe23.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import com.example.tictactoe23.TacViewModel
import com.example.tictactoe23.models.GameStatus
import com.example.tictactoe23.ui.components.GameRestartButton
import com.example.tictactoe23.ui.components.GameScoreRow
import com.example.tictactoe23.ui.components.LineAcrossWinningCells
import com.example.tictactoe23.ui.components.RestartButtonSkin
import com.example.tictactoe23.ui.components.board.Board
import com.example.tictactoe23.ui.components.dialog.GameDialog
import kotlinx.coroutines.delay


private const val mainScreenTag = "mainScreenTag"


@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    viewModel: TacViewModel
) {
    val config = LocalConfiguration.current
    viewModel.setCellSize(config)

    val currentPlayerName = viewModel.currentPlayerName.collectAsState()
    LaunchedEffect(currentPlayerName.value) {
        viewModel.onCurrentPlayerNameChange()
    }

    viewModel.cellSize.value?.let { cellSize ->
        val currentPlayer = viewModel.currentPlayer.observeAsState()
        val isClickabilityToggled = viewModel.isClickabilityToggled.observeAsState()

        if (isClickabilityToggled.value == true) {
            viewModel.swapPlayer()
        }

        currentPlayer.value?.let {
            LaunchedEffect(currentPlayer.value) {
                viewModel.onCurrentPlayerChange()
            }

            val gameStatus = viewModel.gameStatus.observeAsState()
            val isDisplayDialog = viewModel.isDisplayDialog.observeAsState()
            val isDismissDialog = viewModel.isDisplayRestartButton.observeAsState()

            val iconSize = viewModel.iconSize
            val rowWidth = cellSize * viewModel.boardModel!!.columnCount


            Box(
                modifier = modifier
                    .fillMaxSize()
            ) {
                AnimatedVisibility(visible = isDismissDialog.value!! ) {
                    GameRestartButton(
                        size = (viewModel.iconSize*1.5).toInt(),
                        onClick = { viewModel.onGameRestart() },
                        skin = RestartButtonSkin.OnScreen,
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .padding(16.dp)
                    )
                }
                Column(
                    verticalArrangement = Arrangement.spacedBy(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .align(Alignment.Center)
                ) {
                    GameScoreRow(
                        iconSize = iconSize,
                        rowWidth = rowWidth,
                        player1 = viewModel.playerHuman,
                        player2 = viewModel.playerAI,
                        currentPlayer = currentPlayer.value!!
                    )
                    Board(boardModel = viewModel.boardModel!!)
                }
                if(gameStatus.value is GameStatus.GameWin) {
                    LineAcrossWinningCells(
                        gameWin = gameStatus.value!! as GameStatus.GameWin,
                        cellSize = cellSize.toDouble(),
                        animDurationMillis = viewModel.animDurationMillis
                    )
                    LaunchedEffect(Unit) {
                        viewModel.setDisplayDialog(true)
                    }
                }
                AnimatedVisibility(
                    visible = isDisplayDialog.value!!
                ) {
                    GameDialog(
                        gameStatus = gameStatus.value!!,
                        onRestartClick = viewModel::onGameRestart,
                        onDismiss = viewModel::onDialogDismiss
                    )
                }
            }
        }
    }
}
