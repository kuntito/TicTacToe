package com.tito.tictactoe.models

import com.tito.tictactoe.ui.util.UtilityData

/**
 * used to store the result of a check whether the player with [avatarDrawableRes] can win if
 * they play there*/
data class CanWinnerEmergeResult(
    val winnerCanEmerge: Boolean = false,
    val avatarDrawableRes: Int? = null,
    val cell: UtilityData.CellValue? = null
)
