package com.tito.tictactoe.ui.composables

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.Surface
import androidx.compose.material.ripple.LocalRippleTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.*
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tito.tictactoe.models.BoardDetails
import com.tito.tictactoe.models.allBoards
import com.tito.tictactoe.ui.theme.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking

const val boardTag = "boardTag"
@Composable
fun Board(
    boardDetails: BoardDetails,
    /**This flag primarily determines whether the board cells can be clicked*/
    isClickable: Boolean,
    /**This flag in combination with [isClickable] determine whether the board cells can be clicked */
    isGameEnded: Boolean,
    getCurrentPlayerAvatar: (() -> Int)? = null,
    onAvatarVisible: (String, Int) -> Unit = { _, _ -> },
    modifier: Modifier = Modifier
) {

//    boardDetails.width = 308.25
    boardDetails.width = (boardDetails.boardFraction * LocalConfiguration.current.screenWidthDp)
//    boardDetails.width = 0.5 * 411.0
//    Log.d(boardTag, "boardWidth = ${boardDetails.width!!}")
//    Log.d(boardTag, "screenWidth = ${LocalConfiguration.current.screenWidthDp}")
    Surface(
        color = Color.White,
        shape = boardDetails.shape,
        modifier = modifier
            .shadow(elevation = 10.dp, shape = boardDetails.shape)
            .drawWithContent {
                drawContent()
                drawRoundRect(
                    color = Color.White,
                    size = size,
                    style = Stroke(
                        width = ((boardDetails.cellPadding * 4).dp.toPx())
                    ),
                    cornerRadius = CornerRadius(
                        x = boardDetails.cornerRadius.dp.toPx(),
                        y = boardDetails.cornerRadius.dp.toPx()
                    )
                )
            }
    ){
        BoardCells(
            boardDetails = boardDetails,
            isClickable = isClickable,
            isGameEnded = isGameEnded,
            getCurrentPlayerAvatar = getCurrentPlayerAvatar,
            onAvatarVisible = onAvatarVisible
        )
    }
}

@Composable
fun BoardCells(
    boardDetails: BoardDetails,
    isClickable: Boolean,
    isGameEnded: Boolean,
    getCurrentPlayerAvatar: (() -> Int)?,
    onAvatarVisible: (String, Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .padding(boardDetails.cellPadding.dp)
    ) {
        for (rowOfCellValues in boardDetails.cellValues){
            Row {
                for (cellValue in rowOfCellValues){
                    Cell(
                        id = cellValue.id,
                        selectedByAI = cellValue.selectedByAI.value,
                        cellSize = boardDetails.cellSize,
                        cellPadding = boardDetails.cellPadding,
                        getCellCoordinates = {
                            cellValue.coordinates.value = it
                        },
                        isClickableGlobal = isClickable,
                        isGameEnded = isGameEnded,
                        getCurrentPlayerAvatar = getCurrentPlayerAvatar,
                        onAvatarVisible = onAvatarVisible
                    )
                }
            }
        }
    }
}

@Composable
fun Cell(
    id: String,
    selectedByAI: Boolean,
    cellSize: Double,
    cellPadding: Double,
    isClickableGlobal: Boolean,
    isGameEnded: Boolean,
    getCellCoordinates: (Rect) -> Unit,
    getCurrentPlayerAvatar: (() -> Int)?,
    onAvatarVisible: (String, Int) -> Unit,
    modifier: Modifier = Modifier
) {
    var isClickableInternal by remember{ mutableStateOf(false) }
    LaunchedEffect(key1 = isClickableGlobal){
        isClickableInternal = isClickableGlobal
    }
    var currentAvatarDrawable: Int? by remember{ mutableStateOf(null) }
    val onCellClick = {
        Log.d(boardTag, "cell$id clicked")
        currentAvatarDrawable = getCurrentPlayerAvatar!!()
        isClickableInternal = false
    }
    LaunchedEffect(key1 = selectedByAI) {
        if(selectedByAI){
            // AI selects a cell quite fast, so delay makes for a better ux IMO
            delay(1000)
            onCellClick()
        }
    }
    CompositionLocalProvider(LocalRippleTheme provides RippleCustomTheme) {
        Surface(
            color = Blue100,
            modifier = Modifier.padding(cellPadding.dp)
        ) {
            IconButton(
                modifier = modifier
                    .size(cellSize.dp)
                    .onGloballyPositioned {
                        val coordinates = it.boundsInRoot()
                        getCellCoordinates(coordinates)
                        Log.d("customTag", "cell$id onGlobally Positioned")
                    },
                onClick = onCellClick,
//                enabled = false
                enabled = isClickableInternal && !isGameEnded
            ){
                // Icon button is disabled when avatar is placed, LocalContentAlpha is provided to
                // ensure placed avatar isn't greyed out
                CompositionLocalProvider(LocalContentAlpha provides 1f) {
//                AnimatedVisibility(visible = currentAvatarDrawable != null) {
                    if(currentAvatarDrawable != null) {
                        Icon(
                            painter = painterResource(currentAvatarDrawable!!),
                            contentDescription = null,
                            tint = Color.Unspecified,
                            modifier = Modifier
                                .size((cellSize*0.7).dp)
                        )
                        LaunchedEffect(key1 = Unit){
                            onAvatarVisible(id, currentAvatarDrawable!!)
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
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White)
    ) {
        Board(
            boardDetails = allBoards[0],
            isClickable = false,
            isGameEnded = true
        )
    }
}