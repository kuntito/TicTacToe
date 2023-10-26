package com.example.tictactoe23

import android.content.res.Configuration
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.tictactoe23.data.PreferencesRepo
import com.example.tictactoe23.misc.checkWinner
import com.example.tictactoe23.misc.getAppropriateCell
import com.example.tictactoe23.models.BoardCellModel
import com.example.tictactoe23.models.BoardModel
import com.example.tictactoe23.models.GameStatus
import com.example.tictactoe23.models.PlayerAI
import com.example.tictactoe23.models.PlayerHuman
import com.example.tictactoe23.models.PlayerModel
import com.example.tictactoe23.models.StatusName
import com.example.tictactoe23.models.allPlayers
import com.example.tictactoe23.ui.theme.color200
import com.example.tictactoe23.ui.theme.color300
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch


const val viewModelTag = "viewModelTag"
class TacViewModel(
    private val preferencesRepo: PreferencesRepo
): ViewModel() {
    val playerHuman: PlayerModel = PlayerHuman
    val playerAI: PlayerModel = PlayerAI

    /**
     * The player who starts first when the user first launches the game
     * */
    private val initPlayer = PlayerHuman

    var currentPlayerName : StateFlow<String> = preferencesRepo.currentPlayerName
        .map {it}
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = "vibes, but you knew that"
        )

    var currentPlayer: MutableLiveData<PlayerModel> = MutableLiveData()
        private set

    fun saveCurrentPlayerName(playerName: String) {
        viewModelScope.launch {
            preferencesRepo.saveCurrentPlayer(playerName)
            Log.d(viewModelTag, "player saved - $playerName")
        }
    }

    /**
     * [TacViewModel.currentPlayerName] is the source of truth for current player
     * It is a [StateFlow] object persisted using data store. Whenever it's value changes
     * [TacViewModel.currentPlayer] is updated.
     * */
    fun onCurrentPlayerNameChange() {
        val player = allPlayers.find {
            it.label.name == currentPlayerName.value
        }
        Log.d(viewModelTag, "current player changed - ${player?.label?.name}")
        currentPlayer.value = player
    }


    fun swapPlayer() {
        val playerHumanLabel = PlayerHuman.label.name
        val playerAiLabel = PlayerAI.label.name
        when(currentPlayerName.value) {
            playerHumanLabel -> {
                saveCurrentPlayerName(playerAiLabel)
            }
            playerAiLabel -> {
                saveCurrentPlayerName(playerHumanLabel)
            }
        }
        isClickabilityToggled.value = false
    }


    var isClickabilityToggled = MutableLiveData(false)
        private set
    /**
     * This function is called before [TacViewModel.currentPlayer] is swapped
     * If current player is human, cells should be disabled to preventing clicking
     * If current player is ai, cells should be enabled to allow clicking
     * */
    private fun toggleCellClickability() {
        if (currentPlayerName.value == PlayerAI.label.name) {
            boardModel!!.enableVacantCells()
        } else if (currentPlayerName.value == PlayerHuman.label.name) {
            boardModel!!.disableVacantCells()
        }
        isClickabilityToggled.value = true
    }

    /**
     * This is duration of the animation of the line drawn across the winning cells
     * */
    val animDurationMillis = 1000
    val iconSize = 24


    var boardModel: BoardModel? = null
        private set
    var isBoardInitialized = false


    var cellSize: MutableLiveData<Int> = MutableLiveData(null)
        private set

    var isCellsInitialized = false
        private set

    fun setCellSize(
        config: Configuration,
        cellRatio: Float = 0.22F
    ) {
        val screenWidth = config.screenWidthDp
        val screenHeight = config.screenHeightDp

        val smallerDimension = screenWidth.coerceAtMost(screenHeight)

        val size = (smallerDimension * cellRatio).toInt()
        Log.d(viewModelTag, "size set - $size")
        cellSize.value = size

        if (!isBoardInitialized) {
            initializeBoard(size)
            isBoardInitialized = true
        }
    }

    private val initialStatus = GameStatus.Ongoing
    var gameStatus: MutableLiveData<GameStatus?> = MutableLiveData(initialStatus)
        private set

    var gameStatusStatic: GameStatus? = initialStatus
        set(value) {
            field = value
            gameStatus.value = value
        }

    private var _isDisplayDialog = MutableLiveData(false)
    val isDisplayDialog: LiveData<Boolean>
        get() = _isDisplayDialog

    private var _isDisplayRestartButton = MutableLiveData(false)
    val isDisplayRestartButton: LiveData<Boolean>
        get() = _isDisplayRestartButton

    fun onDialogDismiss() {
        setDisplayDialog(false)
        _isDisplayRestartButton.value = true
    }

    fun setDisplayDialog(flag: Boolean) {
        viewModelScope.launch {
            delay(1000L)
            _isDisplayDialog.value = flag
        }
    }

    private fun updateGameStatus(status: GameStatus) {
        if (status != gameStatusStatic) {
            gameStatusStatic = status
        }

        if (status.name in listOf(StatusName.Win, StatusName.Draw)) {
            boardModel!!.disableAllCells()
        }

        if (status is GameStatus.GameWin) {
            currentPlayer.value!!.increaseScore()
        } else if (status is GameStatus.Draw) {
            setDisplayDialog(true)
        }
    }

    private fun onCellClick(cellModel: BoardCellModel) {
        val av = currentPlayer.value!!.avatar
        cellModel.setAvatar(
            av = av
        )
        currentPlayer.value!!.increaseTurnsPlayer()

        boardModel!!.decreaseVacantCells()

        val status = checkWinner(
            boardModel = boardModel!!,
            lastClickedCellAvatar = av,
        )

        updateGameStatus(status)
        toggleCellClickability()
    }


    fun performAIClick() {
        viewModelScope.launch {
            // This simulates a delay before the AI makes its selection
            delay(1800)
            if (currentPlayer.value !is PlayerAI) throw Exception("current player should be AI before" +
                    "`performAIClick can execute`")

            val aiChoice = getAppropriateCell(
                boardModel = boardModel!!,
                playerHuman = playerHuman,
                playerAI = playerAI
            )


            aiChoice?.let {
                val cellModel  = boardModel!!.getCellModel(
                    targetRowIndex = it.rowIndex,
                    targetColumnIndex = it.columnIndex
                )!!
                cellModel.onCellClick(cellModel)

                Log.d(viewModelTag, "appropriate cell is $it")
            }
        }
    }

    fun onCurrentPlayerChange() {
        if (
            currentPlayer.value == PlayerAI &&
            gameStatusStatic is GameStatus.Ongoing
        ) {
            performAIClick()
        }
    }

    private fun onCellsInitialized() {
        isCellsInitialized = true
    }

    private fun initializeBoard(cellSize: Int) {
        // FIXME,The code involving checking for winner and ai's selection were written with
        //  a 3x3 board in mind, a differently sized board might require significant
        //  modifications to the code
        boardModel = BoardModel(
            rowCount = 3,
            columnCount = 3,
            winStreak = 3,
            cellSize = cellSize,
            cellColor = color300,
            borderColor = color200,
            onCellClick = ::onCellClick,
            onCellsInitialized = ::onCellsInitialized
        )
    }

    fun onGameRestart() {
        isBoardInitialized = false
        isCellsInitialized = false
        boardModel = null
        gameStatusStatic = initialStatus
        _isDisplayRestartButton.value = false
        _isDisplayDialog.value = false

        PlayerHuman.resetTurns()
        PlayerAI.resetTurns()

        onCurrentPlayerChange()
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as TacApplication)
                TacViewModel(application.preferencesRepo)
            }
        }
    }
}
