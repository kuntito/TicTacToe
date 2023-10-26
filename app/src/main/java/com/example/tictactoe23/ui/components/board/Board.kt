package com.example.tictactoe23.ui.components.board

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.tictactoe23.models.BoardModel
import com.example.tictactoe23.ui.components.general.preview.PreviewColumn
import com.example.tictactoe23.ui.components.general.preview.prevBoardModel

@Composable
fun Board(
    modifier: Modifier = Modifier,
    boardModel: BoardModel
) {
    Card(
        shape = RoundedCornerShape(boardModel.cornerRadius.dp),
        border = boardModel.borderStroke,
        colors = CardDefaults.cardColors(
            containerColor = boardModel.borderColor,
        ),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .padding(boardModel.borderWidth.dp)
        ) {
            for (rowIndex in 0..boardModel.lastRowIndex) {
                Row {
                    for (columnIndex in 0..boardModel.lastColumnIndex) {
                        val cellDetails = boardModel.getCellModel(
                            rowIndex,
                            columnIndex
                        )
                        cellDetails?.let {
                            BoardCell(cellModel = it)
                        }
                    }
                }
            }
        }
    }
}


@Preview
@Composable
fun BoardPreview() {
    val boardModel = prevBoardModel

    PreviewColumn {
        Board(
            boardModel = boardModel
        )
    }
}