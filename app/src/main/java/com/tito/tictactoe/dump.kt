package com.tito.tictactoe
//
//import android.util.Log
//import androidx.compose.foundation.Canvas
//import androidx.compose.foundation.border
//import androidx.compose.foundation.layout.*
//import androidx.compose.material.IconButton
//import androidx.compose.material.Surface
//import androidx.compose.material.ripple.LocalRippleTheme
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.CompositionLocalProvider
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.draw.shadow
//import androidx.compose.ui.geometry.Offset
//import androidx.compose.ui.geometry.Rect
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.graphics.Shape
//import androidx.compose.ui.layout.boundsInRoot
//import androidx.compose.ui.layout.onGloballyPositioned
//import androidx.compose.ui.platform.LocalConfiguration
//import androidx.compose.ui.tooling.preview.Preview
//import androidx.compose.ui.unit.dp
//import com.tito.tictactoe.models.BoardDetails
//import com.tito.tictactoe.models.allBoards
//import com.tito.tictactoe.ui.theme.Blue100
//import com.tito.tictactoe.ui.theme.RippleCustomTheme
//import com.tito.tictactoe.ui.theme.TicTacToeTheme
//import com.tito.tictactoe.ui.theme.grey200
//import com.tito.tictactoe.ui.util.*
//
////Board
//const val boardTag = "boardTag"
//@Composable
//fun Board(
//    modifier: Modifier = Modifier,
//    isClickable: Boolean = false,
//    board: BoardDetails
//) {
//
//    board.width = (board.boardFraction * LocalConfiguration.current.screenWidthDp)
//
//    Box(
//        modifier = modifier
//            .shadow(elevation = 10.dp, shape = board.shape),
//        contentAlignment = Alignment.Center
//    ){
//        BoardFrame(
//            boardWidth = board.width!!,
//            boardHeight = board.height,
//            lineStroke = board.lineStroke,
//            frameColor = board.frameColor,
//            shape = board.shape
//        )
//        BoardCellsAndLines(
//            board = board,
//            isClickable = isClickable
//        )
//    }
//}
//
//@Composable
//fun BoardFrame(
//    modifier: Modifier = Modifier,
//    frameColor: Color,
//    lineStroke: Double,
//    boardWidth: Double,
//    boardHeight: Double,
//    shape: Shape
//) {
//    Surface(
//        modifier = modifier
//            .border(width = lineStroke.dp, color = frameColor, shape = shape)
//            .width((boardWidth + lineStroke).dp)
//            .height((boardHeight + lineStroke).dp),
////        color = greyFF200,
//        color = frameColor,
//        shape = shape
//    ){}
//}
//
//@Composable
//fun BoardCellsAndLines(
//    modifier: Modifier = Modifier,
//    board: BoardDetails,
//    isClickable: Boolean
//) {
//    val shadowDisplacement = 0.3f
//    Box(modifier = modifier){
//        BoardCells(
//            board = board,
//            isClickable = isClickable
//        )
//    }
//}
//
//@Composable
//fun BoardCells(
//    modifier: Modifier = Modifier,
//    board: BoardDetails,
//    isClickable: Boolean
//) {
//    Column(
//        modifier = modifier
//            .padding(board.cellPadding.dp)
//    ) {
//        for (rowOfCellValues in board.cellValues){
//            Row {
//                for (cellValue in rowOfCellValues){
//                    Cell(
//                        cellId = cellValue.id,
//                        cellSize = board.cellSize,
//                        cellPadding = board.cellPadding,
//                        getCellCoordinates = {
//                            cellValue.coordinates.value = it
//                        },
//                        isClickable = isClickable
//                    )
//                }
//            }
//        }
//    }
//}
//
//
//@Composable
//fun VerticalLines(
//    modifier: Modifier = Modifier,
//    board: BoardDetails,
//    shadowDisplacement: Float,
//    origin: Offset
//) {
//    val lineNumbers = 1 until board.columnsPerRow
//    /**line should overlap the total horizontal padding between each cell*/
//
//    if (board.cellValues.flatten().all { it.coordinates.value != Rect.Zero }) {
//        Canvas(modifier = modifier) {
//            val lineStroke = board.lineStroke.dp.toPx()
//            for(lineNumber in lineNumbers){
//                val x = getVerticalStartX(lineNumber, board.cellValues, origin)
//                val y1 = getVerticalStartY(lineNumber, board.cellValues, origin)
//                val y2 = getVerticalEndY(board.cellValues, origin)
//                // Shadow
//                drawLine(
//                    start = Offset(
//                        x = x+shadowDisplacement*lineStroke,
//                        y = y1
//                    ),
//                    end = Offset(
//                        x = x+shadowDisplacement*lineStroke,
//                        y = y2
//                    ),
//                    strokeWidth = lineStroke,
//                    color = grey200
//                )
//                // Line
//                drawLine(
//                    start = Offset(
//                        x = x,
//                        y = y1
//                    ),
//                    end = Offset(
//                        x = x,
//                        y = y2
//                    ),
//                    strokeWidth = lineStroke,
//                    color = board.lineColor
//                )
//            }
//        }
//    }
//}
//
//
//@Composable
//fun HorizontalLines(
//    board: BoardDetails,
//    modifier: Modifier = Modifier,
//    shadowDisplacement: Float
//) {
//    val lines = 1 until board.rowsPerColumn
//    /**line should overlap the total horizontal padding between each cell*/
//
//    if (board.cellValues.flatten().all { it.coordinates.value != Rect.Zero }){
//        val origin = board.cellValues[0][0].coordinates.value.topLeft
//        Canvas(modifier = modifier){
//            val lineStroke = board.lineStroke.dp.toPx()
//            for(lineNumber in lines){
//                val y = getHorizontalStartY(lineNumber, board.cellValues, origin)
//                val x1 = getHorizontalStartX(lineNumber, board.cellValues, origin)
//                val x2 = getHorizontalEndX(board.cellValues, origin)
//
//                //Shadow
//                drawLine(
//                    start = Offset(
//                        x = x1,
//                        y = y+shadowDisplacement*lineStroke
//                    ),
//                    end = Offset(
//                        x = x2,
//                        y = y+shadowDisplacement*lineStroke
//                    ),
//                    strokeWidth = lineStroke,
//                    color = grey200
//                )
//                // Line
//                drawLine(
//                    start = Offset(
//                        x = x1,
//                        y = y
//                    ),
//                    end = Offset(
//                        x = x2,
//                        y = y
//                    ),
//                    strokeWidth = lineStroke,
//                    color = board.lineColor
//                )
//            }
//        }
//    }
//}
//
//
//
//@Composable
//fun Cell(
//    modifier: Modifier = Modifier,
//    cellId: String,
//    cellSize: Double,
//    cellPadding: Double,
//    isClickable: Boolean,
//    getCellCoordinates: (Rect) -> Unit
//) {
//    CompositionLocalProvider(LocalRippleTheme provides RippleCustomTheme) {
//        Surface(
//            color = Blue100,
//            modifier = Modifier.padding(cellPadding.dp)
//        ) {
//            IconButton(
//                modifier = modifier
//                    .onGloballyPositioned {
//                        val coordinates = it.boundsInRoot()
//                        getCellCoordinates(coordinates)
//                        Log.d(boardTag, "cell coordinates")
//                    }
//                    .size(cellSize.dp),
//                onClick = {},
//                enabled = isClickable
//            ){
//
//            }
//        }
//    }
//}
//
//
//@Preview
//@Composable
//fun BoardPreview() {
//    TicTacToeTheme {
//        Surface{
//            Box(
//                contentAlignment = Alignment.Center,
//                modifier = Modifier.fillMaxSize()
//            ){
//                Board(
//                    board = allBoards[0]
//                )
//            }
//        }
//    }
//}
