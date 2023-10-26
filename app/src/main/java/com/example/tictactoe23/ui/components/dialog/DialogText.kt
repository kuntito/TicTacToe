package com.example.tictactoe23.ui.components.dialog

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.tictactoe23.R
import com.example.tictactoe23.models.GameStatus
import com.example.tictactoe23.models.StatusName
import com.example.tictactoe23.ui.components.AvatarIcon
import com.example.tictactoe23.ui.theme.color200

@Composable
fun DialogText(
    modifier: Modifier = Modifier,
    gameStatus: GameStatus,
    size: Int,
    borderShape: RoundedCornerShape = RoundedCornerShape(16.dp),
    borderColor: Color = color200,
) {
    Card(
        shape = borderShape,
        border = BorderStroke(8.dp, color = borderColor),
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent,
        ),
        modifier = modifier
            .height(148.dp)
            .fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxHeight(0.5f)
                    .fillMaxWidth()
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    val textDrawableScale = if(
                        gameStatus.name == StatusName.Draw
                    ) 4 else 2
                    if (gameStatus.name == StatusName.Win) {
                        AvatarIcon(
                            model = (gameStatus as GameStatus.GameWin).winningAvatar,
                            size = size
                        )
                    }
                    Image(
                        painter = painterResource(
                            id = gameStatus.textDrawable ?: R.drawable.ic_img_placeholder
                        ),
                        contentDescription = "dialog text drawable",
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .size((size*textDrawableScale).dp)
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun DialogTextPreview() {
    val gameStatus = GameStatus.Draw
    DialogText(
        gameStatus = gameStatus,
        size = 40,
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.5f)
    )
}
