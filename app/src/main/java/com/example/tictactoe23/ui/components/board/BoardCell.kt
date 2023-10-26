package com.example.tictactoe23.ui.components.board

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.boundsInRoot
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.tictactoe23.models.BoardCellModel
import com.example.tictactoe23.models.CellPosition
import com.example.tictactoe23.models.PositionType
import com.example.tictactoe23.ui.components.AvatarIcon
import com.example.tictactoe23.ui.components.general.preview.PreviewColumn
import com.example.tictactoe23.ui.components.general.preview.prevBoardModel

fun getRoundedCornerShape(
    positionType: PositionType,
    cornerRadius: Dp = 16.dp
): RoundedCornerShape {
    return when (positionType) {
        PositionType.TopStart -> RoundedCornerShape(topStart = cornerRadius)
        PositionType.TopEnd -> RoundedCornerShape(topEnd = cornerRadius)
        PositionType.BottomStart -> RoundedCornerShape(bottomStart = cornerRadius)
        PositionType.BottomEnd -> RoundedCornerShape(bottomEnd = cornerRadius)
        PositionType.Other -> RoundedCornerShape(0.dp)
    }
}


@Composable
fun BoardCell(
    modifier: Modifier = Modifier,
    cellModel: BoardCellModel,
) {
    val avatar = cellModel.avatarLiveData.observeAsState()
    val isCellClickable = cellModel.isClickable.observeAsState()

    val onCellClick: () -> Unit = {
        cellModel.onCellClick(cellModel)
    }

    Card(
        shape = getRoundedCornerShape(
            cellModel.positionType,
            cellModel.cornerRadius!!.dp
        ),
        border = cellModel.borderStroke,
        colors = CardDefaults.cardColors(
            containerColor = cellModel.color,
        ),
        modifier = modifier
            .onGloballyPositioned {
                val coordinates = it.boundsInRoot()
                cellModel.setCenterCoordinates(coordinates.center)
            }
    ) {
        Box(
            modifier = Modifier
                .size(cellModel.size!!.dp)
                .clickable(
                    enabled = isCellClickable.value!!,
                    onClick = onCellClick
                ),
            contentAlignment = Alignment.Center
        ) {
            avatar.value?.let {
                AvatarIcon(
                    model = it,
                    size = (cellModel.size!! * 0.6f).toInt()
                )
            }
        }
    }
}

@Preview
@Composable
fun BoardCellPreview() {
    val boardModel = prevBoardModel

    val cellModel = BoardCellModel(
        position = CellPosition(2, 1),
        boardModel = boardModel
    )

    PreviewColumn {
        BoardCell(
            cellModel = cellModel
        )
    }
}