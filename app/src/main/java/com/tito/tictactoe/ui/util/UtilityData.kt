package com.tito.tictactoe.ui.util

import androidx.annotation.DrawableRes
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect

object UtilityData {
    data class CellValue(
        var id: String,
        val rowIndex: Int,
        val columnIndex: Int,
        var selectedByAI: MutableState<Boolean> = mutableStateOf(false),
        @DrawableRes var avatarDrawableRes: Int? = null,
        /**The screen coordinates of the composable thes object represents*/
        var coordinates: MutableState<Rect> = mutableStateOf(Rect.Zero),
        var avatarPlaced: Boolean = false
    )

}