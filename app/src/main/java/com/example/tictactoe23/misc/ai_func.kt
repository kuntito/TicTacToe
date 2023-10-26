package com.example.tictactoe23.misc

import android.util.Log
import com.example.tictactoe23.models.BoardLine
import com.example.tictactoe23.models.BoardModel
import com.example.tictactoe23.models.CellPosition
import com.example.tictactoe23.models.PlayerModel
import com.example.tictactoe23.models.PositionType

const val aiFuncTag = "aiFuncTag"

fun isPlayCenter(): Boolean {
    val currentTimeMillis = System.currentTimeMillis().toInt()

    return currentTimeMillis%2 == 0
}

/**
 * Returns the appropriate cell for the AI to select in a Tic-Tac-Toe game.
 *
 * In a 3x3 board, the appropriate cell is determined as follows:
 *
 * - If it's AIs first turn, the center cell (1, 1) is chosen if available.
 * - In other game states, the AI selects the first valid cell that meets one of the following conditions:
 *   1. Completes a winning streak for the AI on its next turn.
 *   2. Prevents the opponent from completing a winning streak on their next turn.
 *   3. Gets the AI closer to forming a winning streak.
 *
 * If none of these conditions are met, the AI selects any vacant cell.
 *
 * @param boardModel An object that models the TicTacToe board
 * @return The coordinates of the appropriate cell as a [CellPosition].
 */
fun getAppropriateCell(
    boardModel: BoardModel,
    playerAI: PlayerModel,
    playerHuman: PlayerModel,
): CellPosition? {

    if ((playerAI.turnsPlayed == 0)) {
        val centerRowIndex = 1
        val centerColumnIndex = 1
        val centerCellModel = boardModel.getCellModel(centerRowIndex, centerColumnIndex)
        val isCenterBlank = centerCellModel!!.avatarStatic == null

        return if (isPlayCenter() && isCenterBlank) {
            CellPosition(centerRowIndex, centerColumnIndex)
        } else {
            getAnyCornerCell(boardModel)
        }
    }

    val appropriateCell = listOf(
        isWinStreakOnNextTurn(
            player = playerAI,
            boardModel = boardModel
        ),
        isWinStreakOnNextTurn(
            player = playerHuman,
            boardModel = boardModel
        ),
        getCloserToWinStreak(
            boardModel = boardModel,
            player = playerAI
        )
    ).firstOrNull { it != null }

    val randomVacantCell = getAnyVacantCell(
        boardModel = boardModel
    ) ?: throw Exception("At this point, a random vacant cell should be" +
            "available because if there were no vacant cells, the game should have ended in a" +
            "draw")

    return appropriateCell ?: randomVacantCell
}

/**
 * Returns a [CellPosition] in which if played completes a [BoardModel.winStreak]
 * */
fun isWinStreakOnNextTurn(
    player: PlayerModel,
    boardModel: BoardModel
): CellPosition? {
    val cellPosition: CellPosition? = null

    val allLines = getAllLines(
        rowCount = boardModel.rowCount,
        columnCount = boardModel.columnCount
    )

    for (line in allLines) {
        val res = canWinNextTurnInLine(
            line=line,
            boardModel = boardModel,
            player = player
        )
        if (res != null) return res
    }

    return cellPosition
}

/**
 * Returns a valid [CellPosition] if condition is found else returns null
 *
 * Looks through a [line] if [BoardModel.winStreak] can occur on the next turn
 *
 * The function is optimized for a 3x3 board, a differently sized board might require a different
 * implementation
 * */
fun canWinNextTurnInLine(
    boardModel: BoardModel,
    player: PlayerModel,
    line: BoardLine,
): CellPosition? {
    var cellPosition: CellPosition? = null
    val penultimateWinStreak = boardModel.winStreak - 1
    val avatar = player.avatar

    var consecCount = 0
    var vacantCellPosition: CellPosition? = null

    val cells = line.cells
    for (position in cells){
        val cell = boardModel.getCellModel(
            targetRowIndex = position.rowIndex,
            targetColumnIndex = position.columnIndex
        )!!

        cell.avatarStatic?.let {
            if (it == avatar) consecCount += 1
        }

        if (cell.avatarStatic == null) {
            vacantCellPosition = position
        }
    }

    if ((vacantCellPosition != null) && (consecCount == penultimateWinStreak)) {
        cellPosition = vacantCellPosition
        Log.d(aiFuncTag, "${player.label.name} can win on $line")
    }

    return cellPosition
}

/**
 * Returns a [CellPosition] that, when selected gets the player closer to forming a winning streak
 *
 * Getting closer to a winning streak means that after the appropriate cell is selected, there are
 * enough vacant cells remaining on the board for the player to potentially achieve a winning streak.
 *
 * The function is optimized for a 3x3 board, a differently sized board might require a different
 * implementation
 */
fun getCloserToWinStreak(
    boardModel: BoardModel,
    player: PlayerModel
): CellPosition? {

    val allLines = getAllLines(
        rowCount = boardModel.rowCount,
        columnCount = boardModel.columnCount
    )

    for (line in allLines){
        val cells = line.cells
        val vacantCells = mutableListOf<CellPosition>()
        var playerAvatarFound = false
        for (cellPosition in cells) {
            val cellDetails = boardModel.getCellModel(
                targetRowIndex = cellPosition.rowIndex,
                targetColumnIndex = cellPosition.columnIndex
            )!!

            if (cellDetails.avatarStatic == player.avatar){
                playerAvatarFound = true
            } else {
                break
            }
            if (cellDetails.avatarStatic == null) {
                vacantCells.add(cellPosition)
            }
        }
        if (playerAvatarFound && (vacantCells.isNotEmpty()))
            return (vacantCells.random())
    }

    return null
}

/**
 * Returns any vacant cell on the board with a preference for corner cells
 * */
fun getAnyVacantCell(
    boardModel: BoardModel,
): CellPosition? {

    val vacantCells = boardModel.getVacantCells()
    val cornerCell = getAnyCornerCell(boardModel)

    return cornerCell ?: vacantCells.randomOrNull()?.position
}

fun getAnyCornerCell(
    boardModel: BoardModel,
): CellPosition? {
    val vacantCells = boardModel.getVacantCells().shuffled()
    val cornerCell = vacantCells.find {cellModel ->
        boardModel.getCellModel(
            targetRowIndex = cellModel.position.rowIndex,
            targetColumnIndex = cellModel.position.columnIndex
        )?.positionType in PositionType.cornerPositions
    }

    return cornerCell?.position
}