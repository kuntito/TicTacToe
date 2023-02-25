package com.tito.tictactoe.ui.composables

import android.util.Log
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.fragment.app.FragmentManager
import androidx.navigation.NavController
import com.tito.tictactoe.R
import com.tito.tictactoe.models.*
import com.tito.tictactoe.models.viewmodel.GameViewModel
import com.tito.tictactoe.ui.fragments.PlayFragment
import com.tito.tictactoe.ui.fragments.appTag
import com.tito.tictactoe.ui.theme.TicTacToeTheme
import com.tito.tictactoe.ui.util.*
import kotlinx.coroutines.delay

@Composable
fun PlayScreen(
    gameViewModel: GameViewModel,
    navController: NavController,
    fragmentManager: FragmentManager
) {
    val currentPlayer = gameViewModel.currentPlayer.observeAsState().value!!
    var aiTurn by mutableStateOf(false)
    val boardDetails = gameViewModel.currentBoard
    var gameStatus by remember{ mutableStateOf(GameStatus.OnGoing) }
    var winner by remember { mutableStateOf(noConsecAvatars)}
    var displayWinnerDialog by remember { mutableStateOf(false) }
    var displayDrawDialog by remember { mutableStateOf(false) }

    if (gameViewModel.currentPlayer.value!! == Players.AI){
        aiTurn = true
    }

    if (aiTurn && gameStatus == GameStatus.OnGoing){
        LaunchedEffect(key1 = Unit){
            aiSelectCell(
                cellValues = boardDetails.cellValues,
                aiAvatar = gameViewModel.currentPlayer.value!!.avatar,
                opponentAvatar = gameViewModel
                    .getOtherPlayer(gameViewModel.currentPlayer.value!!).avatar,
                consecAvatarsToWin = boardDetails.consecAvatarsToWin
            )
        }
        Log.d(boardTag, "outside launched effect")
    }

    val onBackButtonClick = {
        navController.navigateUp()
        Unit
    }

    val getCurrentPlayer = {
        gameViewModel.currentPlayer.value!!.avatar.drawableRes
    }

    val onAvatarVisible = { cellId: String, cellDrawable: Int ->
        gameViewModel.swapPlayer()

        boardDetails.cellValues.flatten().find{ it.id == cellId }!!.let {
            it.avatarPlaced = true
            it.avatarDrawableRes = cellDrawable
        }
        val checkConsec = checkConsecAvatars(
            boardDetails.cellValues,
            boardDetails.consecAvatarsToWin
        )

        if (checkConsec.exists){
            winner = checkConsec
            val playerWithWinningAvatar = gameViewModel.getPlayerWithAvatarDrawable(
                winner.startCell!!.avatarDrawableRes!!
            )
            playerWithWinningAvatar.increaseScore()
            Log.d(appTag, "bug-winningPlayer=$playerWithWinningAvatar")
            Log.d(appTag, "bug-player2=${gameViewModel.player2}")
            gameStatus = GameStatus.WinnerEmerged
        }
        else if (boardDetails.cellValues.flatten().all { it.avatarPlaced }){
            gameStatus = GameStatus.Draw
        }else{
            gameStatus = GameStatus.OnGoing
        }
    }

    val onRestartButtonClick = {
        refreshFragment(
            fragmentContainerId = R.id.fragmentContainerView,
            fragmentManager = fragmentManager,
            fragment = PlayFragment()
        )
    }

    val onDismissRequest = {
        Log.d(boardTag, "onDismiss dialog called $displayWinnerDialog")
        displayWinnerDialog = false
        displayDrawDialog = false
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ){
        PlayScreenContent(
            player1 = gameViewModel.player1,
            player2 = gameViewModel.player2,
            currentPlayer = currentPlayer,
            boardDetails = boardDetails,
            isGameEnded = gameStatus == GameStatus.Draw || gameStatus == GameStatus.WinnerEmerged,
            onBackButtonClick = onBackButtonClick,
            onRestartButtonClick = onRestartButtonClick,
            getCurrentPlayerAvatar = getCurrentPlayer,
            onAvatarVisible = onAvatarVisible
        )
        val animDurationMillis = 500L
        val dialogDisplayDelay = 200L
        when(gameStatus){
            GameStatus.WinnerEmerged -> {
                LineAcrossWinningCells(
                    startCellCenterCoordinates = winner.startCell!!.coordinates.value.center,
                    endCellCenterCoordinates = winner.endCell!!.coordinates.value.center,
                    cellSize = boardDetails.cellSize,
                    animDurationMillis = animDurationMillis.toInt()
                )
                LaunchedEffect(key1 = Unit){
                    delay(animDurationMillis + dialogDisplayDelay)
                    displayWinnerDialog = true
                }
            }
            GameStatus.Draw ->
                LaunchedEffect(key1 = Unit ){
                    delay(dialogDisplayDelay)
                    displayDrawDialog = true
                }
            GameStatus.OnGoing -> {}
        }


        if(displayWinnerDialog){
            GameWinnerDialog(
                winningAvatar = winner.startCell!!.avatarDrawableRes!!,
                onHomeButtonClick = {
                    onBackButtonClick()
                    onDismissRequest()
                },
                onRestartButtonClick = onRestartButtonClick,
                onDismissRequest = onDismissRequest
            )
        }
        if(displayDrawDialog){
            GameDrawDialog(
                onHomeButtonClick = {onBackButtonClick()},
                onRestartButtonClick = onRestartButtonClick,
                onDismissRequest = onDismissRequest
            )
        }
    }
}

@Composable
fun PlayScreenContent(
    player1: Player,
    player2: Player,
    currentPlayer: Player,
    boardDetails: BoardDetails,
    isGameEnded: Boolean,
    onBackButtonClick: () -> Unit,
    onRestartButtonClick: () -> Unit,
    getCurrentPlayerAvatar: () -> Int,
    onAvatarVisible: (String, Int) -> Unit,
    modifier: Modifier = Modifier
) {

    Surface(
        modifier = modifier.fillMaxSize()
    ) {
        ConstraintLayout{
            val (backButton, restartButton, avatarsToWin, playersOverviewAndBoard) = createRefs()
            IconButton(
                onClick ={onBackButtonClick()},
                modifier = Modifier
                    .constrainAs(backButton){
                        top.linkTo(parent.top, margin = 32.dp)
                        start.linkTo(parent.start, margin = 32.dp)
                    }
            ) {
                Icon(
                    painterResource(id = R.drawable.ic_back_button),
                    contentDescription = null,
                    tint = Color.Unspecified,
                    modifier = Modifier
                        .shadow(elevation = 5.dp, shape = CircleShape)
                )
            }
            if(isGameEnded) {
                IconButton(
                    onClick = onRestartButtonClick,
                    modifier = Modifier
                        .constrainAs(restartButton){
                            top.linkTo(backButton.bottom, margin = 16.dp)
                            start.linkTo(backButton.start)
                        }
                ) {
                    Icon(
                        painterResource(id = R.drawable.ic_refresh_button),
                        contentDescription = null,
                        tint = Color.Unspecified,
                        modifier = Modifier
                            .shadow(elevation = 5.dp, shape = CircleShape)
                    )
                }
            }
            AvatarsToWin(
                value = boardDetails.consecAvatarsToWin,
                modifier = Modifier
                    .constrainAs(avatarsToWin){
                        top.linkTo(backButton.top)
                        bottom.linkTo(backButton.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
            )
            Column(
                modifier = Modifier
                    .constrainAs(playersOverviewAndBoard){
                        top.linkTo(backButton.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        bottom.linkTo(parent.bottom)
                    }
            ){

                PlayersOverview(
                    player1 = player1,
                    player2 = player2,
                    currentPlayer = currentPlayer,
                    modifier = Modifier
                        .fillMaxWidth(boardDetails.boardFraction.toFloat())
                )
                Spacer(modifier = Modifier.height(64.dp))
                Board(
                    boardDetails = boardDetails,
                    isClickable = currentPlayer.id == Players.Human1.id,
                    getCurrentPlayerAvatar = getCurrentPlayerAvatar,
                    onAvatarVisible = onAvatarVisible,
                    isGameEnded = isGameEnded,
                    modifier = Modifier
                )
            }
        }
    }
}

@Composable
fun AvatarsToWin(value: Int, modifier: Modifier = Modifier) {
    val iconSize = 20
    Row(
        modifier = modifier
            .graphicsLayer {
                alpha = 0.8f
            }
            .drawWithContent {
                drawContent()
                drawLine(
                    color = Color.White,
                    start = Offset(0f, center.y),
                    end = Offset(size.width, center.y),
                    strokeWidth = (iconSize*0.15).dp.toPx(),
                    cap = StrokeCap.Round
                )
            }
    ){
        (1..value).forEach{ _ ->
            Icon(
                painter = painterResource(Avatars.O.drawableRes),
                contentDescription = null,
                tint = Color.Unspecified,
                modifier = Modifier
                    .size(iconSize.dp)
            )
        }
    }
}

@Composable
fun PlayersOverview(
    player1: Player,
    player2: Player,
    currentPlayer: Player,
    modifier: Modifier = Modifier
) {
    Log.d(appTag, "scoreboard: player1=$player1")
    Log.d(appTag, "scoreboard: player2=$player2")
    val player1Alpha by animateFloatAsState(targetValue = if (player1 == currentPlayer) 1f else 0.6f)
    val player2Alpha by animateFloatAsState(targetValue = if (player2 == currentPlayer) 1f else 0.6f)
    Row(
        modifier = modifier
            .height(70.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ){
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxHeight()
                .graphicsLayer {
                    alpha = player1Alpha
                }
        ){
            PlayerIcon(drawableResource = player1.playerIcon)
            Spacer(modifier = Modifier.width(16.dp))
            PlayerIdAndAvatar(
                player = player1,
                currentPlayer = currentPlayer,
            )
        }
        ScoreBoard(player1Score = player1.score, player2Score = player2.score)
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxHeight()
                .graphicsLayer {
                    alpha = player2Alpha
                }
        ){
            PlayerIdAndAvatar(
                player = player2,
                currentPlayer = currentPlayer
            )
            Spacer(modifier = Modifier.width(16.dp))
            PlayerIcon(drawableResource = player2.playerIcon)
        }
    }
}

@Composable
fun ScoreBoard(
    player1Score: Int,
    player2Score: Int,
    modifier: Modifier = Modifier
) {
    Text(
        text="$player1Score:$player2Score",
        fontSize = 20.sp,
        fontWeight = FontWeight.SemiBold,
        color = Color.White,
        modifier = modifier
    )
}

@Composable
fun PlayerIdAndAvatar(
    player: Player,
    currentPlayer: Player,
    modifier: Modifier = Modifier
) {
    val iconSize by animateDpAsState(
        targetValue = (if (player == currentPlayer) 30 else 20).dp
    )
    val textSize by animateIntAsState(
        targetValue = (if (player == currentPlayer) 20 else 15)
    )
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Text(
            text = player.id,
            fontSize = textSize.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.White
        )
        Spacer(modifier = Modifier.height(16.dp))
        Icon(
            painter = painterResource(player.avatar.drawableRes),
            contentDescription = null,
            tint = Color.Unspecified,
            modifier = Modifier
                .size(iconSize)
        )
    }
}

@Composable
fun LineAcrossWinningCells(
    startCellCenterCoordinates: Offset,
    endCellCenterCoordinates: Offset,
    cellSize: Double,
    animDurationMillis: Int = 500
) {
    val animX = remember { Animatable(startCellCenterCoordinates.x/endCellCenterCoordinates.x) }
    val animY = remember { Animatable(startCellCenterCoordinates.y/endCellCenterCoordinates.y) }
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
    Canvas(modifier = Modifier.fillMaxSize()){
//        val elevation = 10f
//        val shadowCoordinates = getShadowCoordinates(
//            firstCellCoordinates = cell1CenterCoordinates,
//            lastCellCoordinates = cell2CenterCoordinates,
//            elevation = elevation
//        )

        val strokeWidth = (cellSize/4).toFloat()

//        drawLine(
//            color = grey200,
//            start = shadowCoordinates.first(),
//            end = shadowCoordinates.last(),
//            strokeWidth = 0.75f * strokeWidth,
//            cap = StrokeCap.Round
//        )

        drawLine(
            color = Color.White,
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

@Preview(showBackground = true)
@Composable
fun PlayScreenContentPreview() {
    TicTacToeTheme {
        PlayScreenContent(
            player1 = Players.Human1,
            player2 = Players.AI,
            currentPlayer = Players.AI,
            isGameEnded = true,
            onBackButtonClick = {},
            onRestartButtonClick = {},
            getCurrentPlayerAvatar = {0},
            onAvatarVisible = { _, _ -> },
            boardDetails = allBoards[0]
        )
    }
}

@Preview
@Composable
fun PlayersOverviewPreview() {
    PlayersOverview(
        player1 = Players.Human1,
        player2 = Players.AI,
        currentPlayer = Players.AI
    )
}

@Preview
@Composable
fun PlayerAndAvatarPreview() {
    PlayerIdAndAvatar(
        player = Players.Human1,
        currentPlayer = Players.Human1
    )
}

@Preview
@Composable
fun ScoreBoardPreview() {
    ScoreBoard(1, 3)
}

@Preview
@Composable
fun ConsecAvatarsToWinPreview() {
    AvatarsToWin(value = 5)
}