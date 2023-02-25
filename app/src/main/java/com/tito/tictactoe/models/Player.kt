package com.tito.tictactoe.models

import android.util.Log
import com.tito.tictactoe.R

private const val playerTag = "playerTag"
data class Player (
    val id: String,
    val avatar: Avatar,
    val ordinal: Int
){
    val playerIcon: Int
        get() = when(id){
        Players.Human1.id -> R.drawable.ic_human
        Players.AI.id -> R.drawable.ic_ai
        else -> R.drawable.ic_image_placeholder
    }

    private var _score: Int = 0
    val score
        get() = _score

    fun increaseScore(){
        _score += 1
        Log.d(playerTag, "$id, player$ordinal score increased, score=$score")
    }

    fun resetScore(){
        _score = 0
        Log.d(playerTag, "$id, player$ordinal score reset, score=$score")
    }

    override fun toString(): String {
        return "player$ordinal-$id, avatar=$avatar, score=$score"
    }
}

object Players{
    val Human1 = Player(
        "Human",
        avatar = Avatars.X,
        ordinal = 1
    )
    val AI = Player(
        "AI",
        avatar = Avatars.O,
        ordinal = 2
    )
    val Human2 = Player(
        "Human",
        avatar = Avatars.O,
        ordinal = 2
    )

    val allPlayers = listOf(
        Human1, Human2, AI
    )
}