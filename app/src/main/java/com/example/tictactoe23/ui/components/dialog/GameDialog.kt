package com.example.tictactoe23.ui.components.dialog

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.tictactoe23.R
import com.example.tictactoe23.models.AvatarO
import com.example.tictactoe23.models.BoardCellModel
import com.example.tictactoe23.models.CellPosition
import com.example.tictactoe23.models.GameStatus
import com.example.tictactoe23.models.StatusName
import com.example.tictactoe23.ui.components.GameRestartButton
import com.example.tictactoe23.ui.components.RestartButtonSkin
import com.example.tictactoe23.ui.components.general.preview.prevBoardModel
import com.example.tictactoe23.ui.theme.color300

@Composable
fun GameDialog(
    modifier: Modifier = Modifier,
    gameStatus: GameStatus,
    onDismiss: () -> Unit,
    onRestartClick: () -> Unit,
    iconSize: Int = 32,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        shape = RoundedCornerShape(size = 16.dp),
        text = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier
            ) {
                val isDrawDialog = gameStatus.name == StatusName.Draw
                Image(
                    painter = painterResource(
                        id = gameStatus.iconDrawable ?: R.drawable.ic_img_placeholder
                    ),
                    contentDescription = "dialog icon",
                    contentScale = ContentScale.Fit,
                    modifier = modifier
                        .size(
                            if (isDrawDialog) (iconSize * 1.4).dp
                            else iconSize.dp
                        )
                )
                DialogText(
                    gameStatus = gameStatus,
                    size = iconSize
                )
                GameRestartButton(
                    size = iconSize,
                    skin = RestartButtonSkin.OnDialog,
                    onClick = onRestartClick
                )
            }
        },
        confirmButton = {},
        containerColor = color300,
        modifier = modifier
    )
}

@Preview
@Composable
fun GameDrawDialogPreview() {
    GameDialog(
        onRestartClick = {},
        onDismiss = {},
        gameStatus = GameStatus.Draw
    )
}


@Preview
@Composable
fun GameWinDialogPreview() {
    val boardDetails = prevBoardModel
    val cell = BoardCellModel(
        boardModel = prevBoardModel,
        position = CellPosition(0, 0)
    )

    GameDialog(
        onRestartClick = {},
        onDismiss = {},
        gameStatus = GameStatus.GameWin(
            startCell = cell,
            endCell = cell,
            winningAvatar = AvatarO
        )
    )
}