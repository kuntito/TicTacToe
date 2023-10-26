package com.example.tictactoe23.models

import androidx.annotation.DrawableRes
import com.example.tictactoe23.R

enum class StatusName {
    Win,
    Draw,
    Ongoing
}

sealed class GameStatus(
    val name: StatusName,
    @DrawableRes
    val textDrawable: Int?,
    @DrawableRes
    val iconDrawable: Int?
) {
    data class GameWin(
        val startCell: BoardCellModel,
        val endCell: BoardCellModel,
        val winningAvatar: AvatarModel
    ): GameStatus(
        name = StatusName.Win,
        textDrawable = R.drawable.ic_wins_text,
        iconDrawable = R.drawable.ic_crown
    )
    object Draw: GameStatus(
        name = StatusName.Draw,
        textDrawable = R.drawable.ic_draw_text,
        iconDrawable = R.drawable.ic_handshake
    )
    object Ongoing: GameStatus(StatusName.Ongoing, null, null)

    override fun toString(): String {
        return "game ${this.name.name}"
    }
}