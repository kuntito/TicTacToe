package com.tito.tictactoe.ui.composables

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.ripple.LocalRippleTheme
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavController
import com.google.accompanist.pager.*
import com.tito.tictactoe.R
import com.tito.tictactoe.models.BoardDetails
import com.tito.tictactoe.models.GameMode
import com.tito.tictactoe.models.allBoards
import com.tito.tictactoe.models.viewmodel.GameViewModel
import com.tito.tictactoe.ui.fragments.appTag
import com.tito.tictactoe.ui.theme.Blue100
import com.tito.tictactoe.ui.theme.Blue300
import com.tito.tictactoe.ui.theme.RippleCustomTheme
import com.tito.tictactoe.ui.theme.TicTacToeTheme
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue

@OptIn(ExperimentalPagerApi::class)
@Composable
fun HomeScreen(
    gameViewModel: GameViewModel,
    navController: NavController
) {
    val currentBoardIndex = gameViewModel.currentBoardIndex.observeAsState().value!!
    Log.d(appTag, "homescreen currentBoardIndex=$currentBoardIndex")
    HomeScreenContent(
        currentBoardIndex = currentBoardIndex,
        boardPagerState = gameViewModel.boardPagerState,
        setCurrentBoardIndex = gameViewModel::setCurrentBoardIndex,
        previousBoard = gameViewModel::previousBoard,
        nextBoard = gameViewModel::nextBoard,
        onClickOption = {
            gameViewModel.currentGameMode = it
            navController.navigate(R.id.action_homeScreenFragment_to_playFragment)
        },
        boards = gameViewModel.boards
    )
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun HomeScreenContent(
    currentBoardIndex: Int,
    boardPagerState: PagerState,
    setCurrentBoardIndex: (Int) -> Unit,
    previousBoard: () -> Unit,
    nextBoard: () -> Unit,
    onClickOption: (GameMode) -> Unit,
    boards: List<BoardDetails>
) {
    Surface(modifier = Modifier.fillMaxSize()) {
        ConstraintLayout {
            val (mainContent) = createRefs()
            Column(
                modifier = Modifier.constrainAs(mainContent){
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                },
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                BoardPager(
                    currentBoardIndex = currentBoardIndex,
                    setCurrentBoardIndex = setCurrentBoardIndex,
                    pagerState = boardPagerState,
                    boards = boards
                )
                Spacer(modifier = Modifier.height(16.dp))
                BoardControls(
                    boardIndex = currentBoardIndex,
                    boards = boards,
                    previousBoard = previousBoard,
                    nextBoard = nextBoard
                )
                Spacer(modifier = Modifier.height(64.dp))
                GameModeOption(
                    gameMode = GameMode.HumanAI,
                    onClickOption = onClickOption
                )
                Spacer(modifier = Modifier.height(24.dp))
                GameModeOption(
                    gameMode = GameMode.HumanHuman,
                    onClickOption = onClickOption
                )
            }
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun BoardPager(
    modifier: Modifier = Modifier,
    currentBoardIndex: Int,
    pagerState: PagerState,
    setCurrentBoardIndex: (Int) -> Unit,
    boards: List<BoardDetails>
) {
    val coroutineScope = rememberCoroutineScope()

    coroutineScope.launch {
        pagerState.animateScrollToPage(currentBoardIndex)
    }

    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.currentPage }.collect {pageIndex ->
            setCurrentBoardIndex(pageIndex)
        }
    }

    HorizontalPager(
        count = boards.size,
        state = pagerState,
        modifier = modifier
    ) { pageIndex ->
        Board(
            boardDetails = boards[pageIndex].copy(boardFraction = 0.5),
            isClickable = false,
            isGameEnded = true,
            modifier = Modifier.graphicsLayer {
                // Calculate the absolute offset for the current page from the
                // scroll position. We use the absolute value which allows us to mirror
                // any effects for both directions
                val pageOffset = calculateCurrentOffsetForPage(pageIndex).absoluteValue

                // We animate the scaleX + scaleY, between 85% and 100%
                lerp(
                    start = 0.5f,
                    stop = 1f,
                    fraction = 1f - pageOffset.coerceIn(0f, 1f)
                ).also { scale ->
                    scaleX = scale
                    scaleY = scale
                }

                // We animate the alpha, between 50% and 100%
                alpha = lerp(
                    start = 0.5f,
                    stop = 1f,
                    fraction = 1f - pageOffset.coerceIn(0f, 1f)
                )
            }
        )
    }
}

@Composable
fun BoardControls(
    modifier: Modifier = Modifier,
    boardIndex: Int,
    boards: List<BoardDetails>,
    previousBoard: () -> Unit,
    nextBoard: () -> Unit
) {
    val board = boards[boardIndex]
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ){
        IconButton(
            onClick = previousBoard,
            enabled = boardIndex > 0
        ) {
            Icon(
                imageVector = Icons.Filled.KeyboardArrowLeft,
                contentDescription = null
            )
        }
        Text(
            text = buildAnnotatedString {
                append(board.columnsPerRow.toString())
                append(" x ")
                append(board.rowsPerColumn.toString())
            },
            style = MaterialTheme.typography.h5
        )
        IconButton(
            onClick = nextBoard,
            enabled = boardIndex < boards.lastIndex
        ){
            Icon(
                imageVector = Icons.Filled.KeyboardArrowRight,
                contentDescription = null
            )
        }
    }
}

@Composable
fun GameModeOption(
    gameMode: GameMode,
    onClickOption: (GameMode) -> Unit,
    modifier: Modifier = Modifier
) {
    val player1Resource: Int
    val player2Resource: Int
    when(gameMode){
        GameMode.HumanHuman -> {
            player1Resource = R.drawable.ic_human
            player2Resource = R.drawable.ic_human
        }
        GameMode.HumanAI -> {
            player1Resource = R.drawable.ic_human
            player2Resource = R.drawable.ic_ai
        }
    }
    val shape = RoundedCornerShape(20.dp)
    CompositionLocalProvider(LocalRippleTheme provides RippleCustomTheme) {
        Surface(
            modifier = modifier
                .clickable { onClickOption(gameMode) }
                .shadow(elevation = 3.dp, shape = shape),
            shape = shape,
            color = Blue300
        ) {
            Row(
                modifier = Modifier
//                    .width(300.dp)
                    .fillMaxWidth(0.8f)
                    .padding(vertical = 15.dp, horizontal = 20.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                PlayerIcon(drawableResource = player1Resource)
                Text(
                    text = stringResource(id = R.string.versus),
                    style = MaterialTheme.typography.h5,
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold
                )
                PlayerIcon(drawableResource = player2Resource)
            }
        }
    }
}

@Composable
fun PlayerIcon(
    drawableResource: Int,
    modifier: Modifier = Modifier
) {
    val iconSize = 30
    Icon(
        painter = painterResource(id = drawableResource),
        contentDescription = null,
        tint = Color.Unspecified,
        modifier = modifier
            .size(iconSize.dp)
    )
}

/*
@Preview
@Composable
fun PlayOptionsPreview() {
    PlayOptions(
        player1Resource = R.drawable.ic_human,
        player2Resource = R.drawable.ic_ai
    )
}
*/

@OptIn(ExperimentalPagerApi::class)
@Preview
@Composable
fun HomeScreenPreview(){
    TicTacToeTheme {
        HomeScreenContent(
            currentBoardIndex = 0,
            boardPagerState = rememberPagerState(),
            setCurrentBoardIndex = {},
            previousBoard = {},
            nextBoard = {},
            onClickOption = {},
            boards = allBoards
        )
    }
}
