package com.tito.tictactoe.models

import com.tito.tictactoe.ui.util.UtilityData.CellValue

data class CellAndAdjacents(
    val cell: CellValue,
    val adjacentCells: List<CellValue>
){
    val vacantAdjacentCells = adjacentCells.filter {
        it.avatarDrawableRes == null
    }

    val adjacentCellsOccupiedByCellAvatar: List<CellValue>
        get(){
            if (cell.avatarDrawableRes != null){
                return adjacentCells.filter {
                    it.avatarDrawableRes == cell.avatarDrawableRes
                }
            }
            return emptyList()
        }

    val adjacentCellsOccupiedByOtherAvatar = adjacentCells.filter {
        it.avatarDrawableRes != null &&
                it.avatarDrawableRes != cell.avatarDrawableRes
    }
}
