package com.example.tictactoe23.misc

import com.example.tictactoe23.models.BoardLine
import com.example.tictactoe23.models.CellPosition
import com.example.tictactoe23.models.LineType

private fun getVerticalLines(
    rowCount: Int,
    columnCount: Int,
): List<BoardLine> {
    val lines = mutableListOf<BoardLine>()

    val startRowIndex = 0
    for (columnIndex in 0 until columnCount) {
        val verticalLine = BoardLine(lineType = LineType.Vertical)
        for (rowIndex in startRowIndex until rowCount) {
            verticalLine.cells.add(
                CellPosition(rowIndex, columnIndex)
            )
        }
        lines.add(verticalLine)
    }

    return lines
}

private fun getHorizontalLines(
    rowCount: Int,
    columnCount: Int
): List<BoardLine> {
    val lines = mutableListOf<BoardLine>()

    val startColumnIndex = 0
    for (rowIndex in 0 until rowCount) {
        val horizontalLine = BoardLine(lineType = LineType.Horizontal)
        for (columnIndex in startColumnIndex until columnCount) {
            horizontalLine.cells.add(
                CellPosition(rowIndex, columnIndex)
            )
        }
        lines.add(horizontalLine)
    }

    return lines.toList()
}

private fun getBackwardSlantingDiagonals(
    rowCount: Int,
    columnCount: Int,
): List<BoardLine> {
    val bsds = mutableListOf<BoardLine>()

    var rowIndex = 0
    var columnIndex = 0

    // FIXME On a 3x3 board, there is only one forward slanting diagonal that can contain a win streak
    //  i.e. (00, 11, 22)
    //  If the board was larger, the algorithm would have to be rewritten to
    //  accomodate the larger board
    if ((rowCount != 3) or (columnCount != 3)) throw Exception(
        "board is not 3x3, " +
                "forward slanting diagonals can not be determined"
    )

    val bsdLine = BoardLine(lineType = LineType.BSD)
    for (i in 1..rowCount) {
        bsdLine.cells.add(
            CellPosition(rowIndex, columnIndex)
        )
        rowIndex += 1
        columnIndex += 1
    }
    bsds.add(bsdLine)

    return bsds
}


private fun getForwardSlantingDiagonals(
    rowCount: Int,
    columnCount: Int,
): List<BoardLine> {
    val fsds = mutableListOf<BoardLine>()

    var rowIndex = 0
    var columnIndex = rowCount - 1

    // FIXME, On a 3x3 board, there is only one backward slanting diagonal that can contain a win streak
    //  i.e. (02, 11, 20)
    //  If the board was larger, the algorithm would have to be rewritten to
    //  accomodate the larger board
    if ((rowCount != 3) or (columnCount != 3)) throw Exception("board is not 3x3, " +
            "forward slanting diagonals can not be determined")

    val fsdLine = BoardLine(lineType = LineType.FSD)
    for (i in 1 .. rowCount) {
        fsdLine.cells.add(
            CellPosition(rowIndex, columnIndex)
        )
        rowIndex += 1
        columnIndex -= 1
    }
    fsds.add(fsdLine)

    return fsds
}

fun getAllLines(
    rowCount: Int,
    columnCount: Int,
): List<BoardLine> {
    val lines = mutableListOf<BoardLine>()
    val verticalLines = getVerticalLines(
        rowCount = rowCount,
        columnCount = columnCount,
    )

    val horizontalLines = getHorizontalLines(
        rowCount = rowCount,
        columnCount = columnCount,
    )

    val bsdLines = getBackwardSlantingDiagonals(
        rowCount = rowCount,
        columnCount = columnCount,
    )

    val fsdLines = getForwardSlantingDiagonals(
        rowCount = rowCount,
        columnCount = columnCount
    )

    lines.addAll(verticalLines)
    lines.addAll(horizontalLines)
    lines.addAll(bsdLines)
    lines.addAll(fsdLines)

    return lines
}