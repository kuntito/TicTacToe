package com.tito.tictactoe.models

import androidx.annotation.DrawableRes
import com.tito.tictactoe.R


enum class AvatarTypes{
    X, O
}

data class Avatar(
    val avatarType: AvatarTypes,
    @DrawableRes val drawableRes: Int
)

object Avatars{
    val X = Avatar(
        AvatarTypes.X,
        R.drawable.ic_x
    )

    val O = Avatar(
        AvatarTypes.O,
        R.drawable.ic_o
    )
}
