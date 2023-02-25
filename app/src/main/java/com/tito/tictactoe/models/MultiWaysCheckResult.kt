package com.tito.tictactoe.models

import com.tito.tictactoe.ui.util.UtilityData.CellValue

data class MultiWaysCheckResult(
    val exists: Boolean = false,
    /**cells where opponent avatar can play to block multi ways*/
    val blockingCells: List<CellValue> = emptyList()
)
