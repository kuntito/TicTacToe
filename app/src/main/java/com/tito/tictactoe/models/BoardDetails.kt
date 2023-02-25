package com.tito.tictactoe.models

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.tito.tictactoe.ui.theme.Blue100
import com.tito.tictactoe.ui.util.UtilityData.CellValue
import com.tito.tictactoe.ui.util.getCellSize
import com.tito.tictactoe.ui.util.initializeCellValues

data class BoardDetails(
    val columnsPerRow: Int,
    val rowsPerColumn: Int,
    val consecAvatarsToWin: Int,
    /** fraction of screen's width board should occupy*/
    val boardFraction: Double = 0.75,
    var width: Double? = null,
    val cellColor: Color = Blue100,
    val lineColor: Color = Color.White,
    val frameColor: Color = Color.White
){
    private var _cellValues = initializeCellValues(columnsPerRow, rowsPerColumn)

    /**
     * A collection that consists of rows of cell values (details about each cell)
     * on the tictactoe board
     */
    val cellValues
        get() = _cellValues

    fun resetBoard(){
        _cellValues = initializeCellValues(columnsPerRow, rowsPerColumn)
    }

    val cellSize
        get() = width?.let {
        getCellSize(cellPaddingFraction, columnsPerRow, it)
    } ?: 0.0

    private val cellPaddingFraction = 0.05
    val cellPadding
        get() = cellSize * cellPaddingFraction

    val lineStroke
        get() = cellPadding * 2.5

    val cornerRadius
        get() = lineStroke * 1.5

    val shape
        get() = RoundedCornerShape(cornerRadius.dp)

    val height
        get() = cellSize*rowsPerColumn + cellPadding*2 * (rowsPerColumn + 1)
}


val allBoards = listOf(
    BoardDetails(
        columnsPerRow = 3,
        rowsPerColumn = 3,
        consecAvatarsToWin = 3
    ),
    BoardDetails(
        columnsPerRow = 6,
        rowsPerColumn = 6,
        consecAvatarsToWin = 4
    ),
    BoardDetails(
        columnsPerRow = 9,
        rowsPerColumn = 9,
        consecAvatarsToWin = 5
    )
)