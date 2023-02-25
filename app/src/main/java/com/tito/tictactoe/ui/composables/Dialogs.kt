package com.tito.tictactoe.ui.composables

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Surface
import androidx.compose.material.ripple.LocalRippleTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.tito.tictactoe.R
import com.tito.tictactoe.ui.theme.Blue100
import com.tito.tictactoe.ui.theme.Blue200
import com.tito.tictactoe.ui.theme.RippleCustomTheme


@Composable
fun GameWinnerDialog(
    @DrawableRes winningAvatar: Int,
    onHomeButtonClick: () -> Unit,
    onRestartButtonClick: () -> Unit,
    onDismissRequest: () -> Unit
) {
    AlertDialog(
        //TODO tapping outside the dialog shouldn't dismiss it, user should use buttons to navigate
        onDismissRequest = onDismissRequest,
        shape = RoundedCornerShape(20.dp),
        text = {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                DialogTextOnWin(winningAvatar = winningAvatar)
                Spacer(modifier = Modifier.height(20.dp))
                DialogButton(
                    iconDrawableRes = R.drawable.ic_home,
                    onClick = onHomeButtonClick,
                )
                Spacer(modifier = Modifier.height(16.dp))
                DialogButton(
                    iconDrawableRes = R.drawable.ic_refresh,
                    onClick = onRestartButtonClick,
                )
            }
        },
        modifier = Modifier
            .wrapContentSize()
            .width(300.dp),
        backgroundColor = Blue200,
        buttons = {}
    )
}


@Composable
fun DialogTextOnWin(@DrawableRes winningAvatar: Int){
    ConstraintLayout(modifier = Modifier
        .wrapContentHeight()
        .fillMaxWidth()) {
        val (frame, winsRow) = createRefs()
        Image(
            painter = painterResource(R.drawable.ic_winner_frame),
            contentDescription = null,
            contentScale = ContentScale.FillBounds,
            modifier = Modifier
                .constrainAs(frame) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
                .width(200.dp)
                .height(117.dp)
        )
        Row(
            modifier = Modifier.constrainAs(winsRow) {
                top.linkTo(frame.top, margin = 25.dp)
                start.linkTo(frame.start)
                end.linkTo(frame.end)
                bottom.linkTo(frame.bottom)
            },
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painterResource(id = winningAvatar),
                contentDescription = null,
                tint = Color.Unspecified,
                modifier = Modifier.size(30.dp)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Image(
                painter = painterResource(R.drawable.ic_wins),
                contentDescription = null,
                modifier = Modifier
                    .width(82.dp)
                    .height(21.dp)
            )
        }
    }
}

@Composable
fun GameDrawDialog(
    onHomeButtonClick: () -> Unit,
    onRestartButtonClick: () -> Unit,
    onDismissRequest: () -> Unit
) {
    AlertDialog(
        //TODO tapping outside the dialog shouldn't dismiss it, user should use buttons to navigate
        onDismissRequest = onDismissRequest,
        shape = RoundedCornerShape(20.dp),
        text = {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                DialogTextOnDraw()
                Spacer(modifier = Modifier.height(20.dp))
                DialogButton(
                    iconDrawableRes = R.drawable.ic_home,
                    onClick = onHomeButtonClick,
                )
                Spacer(modifier = Modifier.height(16.dp))
                DialogButton(
                    iconDrawableRes = R.drawable.ic_refresh,
                    onClick = onRestartButtonClick,
                )
            }
        },
        modifier = Modifier
            .wrapContentSize()
            .width(300.dp),
        backgroundColor = Blue200,
        buttons = {}
    )
}

@Composable
fun DialogTextOnDraw() {
    ConstraintLayout(modifier = Modifier
        .wrapContentHeight()
        .fillMaxWidth()) {
        val (frame, textBox) = createRefs()
        Image(
            painter = painterResource(R.drawable.ic_draw_frame),
            contentDescription = null,
            contentScale = ContentScale.FillBounds,
            modifier = Modifier
                .constrainAs(frame) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
                .width(200.dp)
                .height(117.dp)
        )
        Box(
            modifier = Modifier.constrainAs(textBox) {
                top.linkTo(frame.top, margin = 25.dp)
                start.linkTo(frame.start)
                end.linkTo(frame.end)
                bottom.linkTo(frame.bottom)
            }
        ){
            Icon(
                painterResource(id = R.drawable.ic_draw_text),
                contentDescription = null,
                tint = Color.Unspecified,
                modifier = Modifier.size(100.dp)
            )
        }
    }
}

@Composable
fun DialogButton(
    @DrawableRes iconDrawableRes: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    CompositionLocalProvider(LocalRippleTheme provides RippleCustomTheme) {
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = Blue100,
            modifier = modifier
        ){
            IconButton(
                onClick = onClick,
                modifier = Modifier
                    .width(70.dp)
                    .height(36.dp)
            ){
                Icon(
                    painter = painterResource(id = iconDrawableRes),
                    contentDescription = null,
                    tint = Color.Unspecified,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}

@Preview
@Composable
fun DialogTextOnDrawPreview() {
    DialogTextOnDraw()
}

@Preview
@Composable
fun DialogTextOnWinPreview() {
    DialogTextOnWin(
        winningAvatar = R.drawable.ic_o
    )
}



@Preview
@Composable
fun GameWinnerDialogPreview() {
    GameWinnerDialog(
        winningAvatar = R.drawable.ic_o,
        onHomeButtonClick = {},
        onRestartButtonClick = {},
        onDismissRequest = {}
    )
}


@Preview
@Composable
fun GameDrawDialogPreview() {
    GameDrawDialog(
        onHomeButtonClick = {},
        onRestartButtonClick = {},
        onDismissRequest = {}
    )
}

