package com.example.tictactoe23.ui.components.general.preview

import com.example.tictactoe23.models.BoardModel
import com.example.tictactoe23.ui.theme.color200
import com.example.tictactoe23.ui.theme.color300

val prevBoardModel = BoardModel(
    rowCount = 3,
    columnCount = 3,
    winStreak = 3,
    cellSize = 90,
    cellColor = color300,
    borderColor = color200,
    onCellClick = {},
    onCellsInitialized = {}
)