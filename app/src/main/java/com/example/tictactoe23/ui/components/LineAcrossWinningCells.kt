package com.example.tictactoe23.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import com.example.tictactoe23.models.GameStatus
import com.example.tictactoe23.ui.theme.color200

@Composable
fun LineAcrossWinningCells(
    modifier: Modifier = Modifier,
    gameWin: GameStatus.GameWin,
    cellSize: Double,
    lineColor: Color = color200,
    animDurationMillis: Int
) {
    val startCellCenterCoordinates = gameWin.startCell.centerCoordinates
    val endCellCenterCoordinates = gameWin.endCell.centerCoordinates
    val animX = remember { Animatable(
        startCellCenterCoordinates.x/endCellCenterCoordinates.x)
    }
    val animY = remember { Animatable(
        startCellCenterCoordinates.y/endCellCenterCoordinates.y)
    }
    val easing = FastOutLinearInEasing
    LaunchedEffect(animX) {
        animX.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = animDurationMillis, easing = easing)
        )
    }
    LaunchedEffect(animY) {
        animY.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = animDurationMillis, easing = easing)
        )
    }
    Canvas(modifier = modifier.fillMaxSize()){
        val strokeWidth = (cellSize/4).toFloat()

        drawLine(
            color = lineColor,
            start = startCellCenterCoordinates,
            end = Offset(
                animX.value * endCellCenterCoordinates.x,
                animY.value * endCellCenterCoordinates.y
            ),
            strokeWidth = strokeWidth,
            cap = StrokeCap.Round
        )
    }
}