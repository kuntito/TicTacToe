package com.tito.tictactoe.models.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.PagerState
import com.tito.tictactoe.models.*

private const val viewModelTag = "viewModelTag"
class GameViewModel: ViewModel() {
//    var currentGameMode: GameMode? = null
    var currentGameMode: GameMode = GameMode.HumanHuman
        set(value){
        field = value
        Log.d(viewModelTag, "gameMode=$currentGameMode")
        if (_currentPlayer.value == null){
            _currentPlayer.value = players.find {
                it.ordinal == currentPlayerOrdinal
            }
        }else{
            resetCurrentPlayer()
        }
        resetPlayerScores()
    }

    private fun resetPlayerScores() {
        for(player in players){
            player.resetScore()
        }
    }

    private val players
        get() = listOf(player1, player2)

    private var currentPlayerOrdinal = 1

    private val _currentBoardIndex =  MutableLiveData<Int>()
    val currentBoardIndex: LiveData<Int>
        get() = _currentBoardIndex


    val boards = allBoards
    @OptIn(ExperimentalPagerApi::class)
    val boardPagerState by lazy { PagerState(_currentBoardIndex.value!!) }

    val currentBoard: BoardDetails
        get() = boards[currentBoardIndex.value!!]

    fun setCurrentBoardIndex(value: Int){
        _currentBoardIndex.value = value
    }

    fun previousBoard(){
        if (_currentBoardIndex.value!! > 0){
            setCurrentBoardIndex(_currentBoardIndex.value!! - 1)
        }
    }

    fun nextBoard(){
        if(_currentBoardIndex.value!! < boards.lastIndex){
            setCurrentBoardIndex(_currentBoardIndex.value!! + 1)
        }
    }

    val player1: Player
        get(){
            return when(currentGameMode){
                GameMode.HumanHuman -> Players.Human1
                GameMode.HumanAI -> Players.Human1
            }
        }
    val player2: Player
        get(){
            return when(currentGameMode){
                GameMode.HumanHuman -> Players.Human2
                GameMode.HumanAI -> Players.AI
            }
        }
    private var _currentPlayer = MutableLiveData<Player>()
    val currentPlayer: LiveData<Player>
        get() = _currentPlayer

    fun swapPlayer(){
        _currentPlayer.value = when(_currentPlayer.value!!){
            player1 -> player2
            player2 -> player1
            else -> {throw Exception("_currentPlayer=${currentPlayer.value} is " +
                    "neither player1=$player1 or player2=$player2")}
        }
        Log.d(viewModelTag, "playerSwapped, currentPlayer=${currentPlayer.value}")
    }

    fun setCurrentPlayerOrdinal(value: Int){
        if (value in Players.allPlayers.map { it.ordinal }){
            currentPlayerOrdinal = value
        }
    }

    private fun resetCurrentPlayer(){
        val currentPlayerAvatar = _currentPlayer.value!!.avatar

        _currentPlayer.value = players.find{
            it.avatar == currentPlayerAvatar
        }?: throw Exception("currentPlayer was not reset, currentPlayerAvatar was not found")
    }

    fun getOtherPlayer(player: Player): Player{
        if (players.size!=2) throw Exception("list of players != 2, cannot get other player")
        return players.find {
            it.avatar != player.avatar
        }!!
    }

    fun getPlayerWithAvatarDrawable(avatarDrawable: Int): Player{
        val playerFound = players.find{
            it.avatar.drawableRes == avatarDrawable
        }!!
        Log.d(viewModelTag, "playerFound=$playerFound")
        return playerFound
    }
}