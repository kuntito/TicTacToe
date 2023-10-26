package com.example.tictactoe23.models

import androidx.compose.ui.geometry.Offset
import androidx.lifecycle.MutableLiveData

data class CellPosition(
    val rowIndex: Int,
    val columnIndex: Int,
)

data class BoardCellModel(
    val boardModel: BoardModel,
    val position: CellPosition
) {
    val positionType = boardModel.getPositionType(position)

    val cornerRadius = boardModel.cornerRadius
    val size = boardModel.cellSize
    val borderStroke = boardModel.borderStroke
    val color = boardModel.cellColor
    val onCellClick: (BoardCellModel) -> Unit = {
        boardModel.onCellClick(it)
        setIsClickable(false)
    }

    var avatarLiveData: MutableLiveData<AvatarModel?> = MutableLiveData(null)
        private set

    var avatarStatic: AvatarModel? = null
        set(value) {
            field = value
            avatarLiveData.value = value
        }

    fun setAvatar(av: AvatarModel) {
        avatarStatic = av
    }

    var centerCoordinates: Offset = Offset.Unspecified
        private set

    fun setCenterCoordinates(pos: Offset) {
        centerCoordinates = pos
    }

    var isClickable: MutableLiveData<Boolean> = MutableLiveData(true)
        private set

    fun setIsClickable(flag: Boolean) {
        isClickable.postValue(flag)
    }

    override fun toString(): String {
        return """
            positionType - $positionType,
            cornerRadius - $cornerRadius,
            size - $size,
            borderStroke - $borderStroke,
            color - $color
        """.trimIndent()
    }
}
