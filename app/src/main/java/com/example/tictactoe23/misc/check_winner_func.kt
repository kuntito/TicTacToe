package com.example.tictactoe23.misc

import com.example.tictactoe23.models.AvatarModel
import com.example.tictactoe23.models.BoardCellModel
import com.example.tictactoe23.models.BoardLine
import com.example.tictactoe23.models.BoardModel
import com.example.tictactoe23.models.GameStatus
import com.example.tictactoe23.models.StatusName

private const val checkWinnerTag = "checkWinnerTag"
private fun isWinStreakOnLine(
    line: BoardLine,
    avatar: AvatarModel,
    winStreak: Int,
    getCell: (Int, Int) -> BoardCellModel?
): Boolean {
    var streak = 1

    val cells = line.cells
    if (cells.size >= 2) {
        for (index in 1 until cells.size) {
            val cellPosition = cells[index]
            val (rowIndex, columnIndex) = cellPosition
            val cell = getCell(rowIndex, columnIndex)!!

            val previousCellPosition = cells[index - 1]
            val previousCell = getCell(
                previousCellPosition.rowIndex,
                previousCellPosition.columnIndex
            )!!


            if ((cell.avatarStatic == avatar) && (previousCell.avatarStatic == avatar)) {
                streak += 1
            } else if (cell.avatarStatic == avatar) {
                streak = 1
            } else {
                streak = 0
            }
            if (streak == winStreak) break
        }
    }


    return streak == winStreak
}

fun checkWinner(
    lastClickedCellAvatar: AvatarModel,
    boardModel: BoardModel
): GameStatus {
    var gameStatus: GameStatus = GameStatus.Ongoing

    val rowCount = boardModel.rowCount
    val columnCount = boardModel.columnCount

    val allLines = getAllLines(
        rowCount = rowCount,
        columnCount = columnCount,
    )

    for (line in allLines) {
        if (isWinStreakOnLine(
                line = line,
                avatar = lastClickedCellAvatar,
                winStreak = boardModel.winStreak,
                getCell = boardModel::getCellModel
            )
        ) {
            val cells = line.cells
            val startCell = boardModel.getCellModel(
                targetRowIndex = cells.first().rowIndex,
                targetColumnIndex = cells.first().columnIndex
            )
            val endCell = boardModel.getCellModel(
                targetRowIndex = cells.last().rowIndex,
                targetColumnIndex = cells.last().columnIndex
            )
            gameStatus = GameStatus.GameWin(
                startCell = startCell!!,
                endCell = endCell!!,
                winningAvatar = lastClickedCellAvatar
            )
        }
    }

    if (gameStatus.name != StatusName.Win && (boardModel.vacantCellsCount == 0)) {
        gameStatus = GameStatus.Draw
    }

    return gameStatus
}