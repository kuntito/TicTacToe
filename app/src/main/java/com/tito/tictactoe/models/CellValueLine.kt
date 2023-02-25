package com.tito.tictactoe.models

import com.tito.tictactoe.ui.util.UtilityData.CellValue

/**a data class that stores the sequential cells on a specific [LineType]*/
data class CellValueLine(
    val cellValuesList: List<CellValue>,
    val lineType: LineType
){
    /**returns all cells with avatars unless [avatarDrawableRes] has a value.
     * returns empty list if avatar is not found*/
    fun getCellsWithAvatar(avatarDrawableRes: Int?): List<CellValue>{
        avatarDrawableRes?.let {
            return cellValuesList.filter {
                it.avatarDrawableRes == avatarDrawableRes
            }
        }

        return cellValuesList.filter{
            it.avatarDrawableRes != null
        }
    }

    /**searches [cellValuesList] and finds cellValues at specific position,
     * position=n means the nth cellValue after the [cellValue] passed,
     * position=-n means the nth cellValue before [cellValue]
     * position=0 returns the [cellValue] passed if it exists in [cellValuesList] */
    fun findCell(cellValue: CellValue, position: Int): CellValue? {
        val cellWithIndex = cellValuesList.withIndex().find {
            it.value == cellValue
        }

        cellWithIndex?.let {
            if (position == 0) return cellValue

            val index = it.index+position
            if(index in cellValuesList.indices) {
                return cellValuesList[index]
            }
        }
        return null
    }

    fun vacantCells(): List<CellValue> = cellValuesList.filter { it.avatarDrawableRes == null }


    fun isThereVacantCellAround(cellValue: CellValue): CellValue?{
        val cellBefore = findCell(cellValue, position = -1)
        val cellAfter = findCell(cellValue, position = 1)

        cellBefore?.let {
            if (it in vacantCells()){
                return it
            }
        }

        cellAfter?.let {
            if (it in vacantCells()){
                return it
            }
        }

        return null
    }
}

