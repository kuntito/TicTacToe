package com.example.tictactoe23.models

import androidx.annotation.DrawableRes
import com.example.tictactoe23.R

enum class PlayerLabelType {
    Human,
    AI
}

abstract class PlayerModel(
    val label: PlayerLabelType,
    @DrawableRes
    val iconDrawable: Int,
    val avatar : AvatarModel,
) {
    var score: Int = 0
        private set

    var turnsPlayed: Int = 0
        private set

    fun increaseScore() {
        score += 1
    }

    fun increaseTurnsPlayer() {
        turnsPlayed += 1
    }

    fun resetTurns() {
        turnsPlayed = 0
    }
}

object PlayerHuman: PlayerModel(
    label = PlayerLabelType.Human,
    iconDrawable = R.drawable.ic_human_shadow,
    avatar = AvatarO
)

object PlayerAI: PlayerModel(
    label = PlayerLabelType.AI,
    iconDrawable = R.drawable.ic_ai_shadow,
    avatar = AvatarX
)

val allPlayers = listOf(
    PlayerHuman,
    PlayerAI
)