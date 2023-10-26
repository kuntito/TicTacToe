package com.example.tictactoe23.models

import androidx.compose.foundation.BorderStroke
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

enum class PositionType {
    TopStart,
    TopEnd,
    BottomStart,
    BottomEnd,
    Other;

    companion object {
        val cornerPositions = listOf(
            TopStart,
            TopEnd,
            BottomStart,
            BottomEnd,
        )
    }
}

class BoardModel (
    val rowCount: Int,
    val columnCount: Int,
    val winStreak: Int,
    var cellSize: Int,
    val cellColor: Color,
    val borderColor: Color,
    val onCellClick: (BoardCellModel) -> Unit,
    val onCellsInitialized: () -> Unit,
    val cornerRadiusRatio: Float = 0.2f,
    val borderRatio: Float = 0.0625f,
) {

    var cornerRadius = (cellSize * cornerRadiusRatio).toInt()
    var borderWidth = (cellSize * borderRatio).toInt()
    var borderStroke = BorderStroke(borderWidth.dp, borderColor)

    val lastColumnIndex = columnCount - 1
    val lastRowIndex = rowCount - 1

    private var isCellsInitialized = false



    private val allCells: MutableList<List<BoardCellModel>> = mutableListOf()
    var vacantCellsCount = rowCount * columnCount
        private set

    fun decreaseVacantCells() {
        vacantCellsCount -= 1
    }

    private fun initializeCells() {
        if (!isCellsInitialized) {
            for (rowIndex in 0..lastRowIndex) {
                val cellRow = mutableListOf<BoardCellModel>()
                for (columnIndex in 0..lastColumnIndex) {
                    cellRow.add(
                        BoardCellModel(
                            boardModel = this,
                            position = CellPosition(
                                rowIndex = rowIndex,
                                columnIndex = columnIndex
                            )
                        )
                    )
                }
                allCells.add(cellRow.toList())
            }
            onCellsInitialized()
            isCellsInitialized = true
        }
    }


    fun getPositionType(position: CellPosition) : PositionType {
        val (rowIndex, columnIndex) = position
        return when {
            (rowIndex == 0 && columnIndex == 0) -> PositionType.TopStart
            (rowIndex == 0 && columnIndex == lastColumnIndex) -> PositionType.TopEnd
            (rowIndex == lastRowIndex && columnIndex == 0) -> PositionType.BottomStart
            (rowIndex == lastRowIndex && columnIndex == lastColumnIndex) -> PositionType.BottomEnd
            else -> PositionType.Other
        }
    }

    fun getCellModel(targetRowIndex: Int, targetColumnIndex: Int): BoardCellModel? {
        return try {
            allCells[targetRowIndex][targetColumnIndex]
        } catch (e: IndexOutOfBoundsException) {
            null
        }
    }

    fun disableAllCells() {
        for (row in allCells) {
            for (cell in row) {
                cell.setIsClickable(false)
            }
        }
    }

    fun getVacantCells(): List<BoardCellModel> {
        val vacantCells = mutableListOf<BoardCellModel>()
        for (row in allCells) {
            for (cell in row) {
                if (cell.avatarStatic == null) {
                    vacantCells.add(cell)
                }
            }
        }

        return vacantCells
    }

    fun disableVacantCells() {
        val vacantCells = getVacantCells()
        for (cell in vacantCells) {
            cell.setIsClickable(false)
        }
    }

    fun enableVacantCells() {
        val vacantCells = getVacantCells()
        for (cell in vacantCells) {
            cell.setIsClickable(true)
        }
    }

    init {
        initializeCells()
    }
}