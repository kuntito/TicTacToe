package com.example.tictactoe23.models

import androidx.annotation.DrawableRes
import com.example.tictactoe23.R

enum class AvatarType {
    X,
    O
}

abstract class AvatarModel(
    @DrawableRes
    val iconDrawable: Int,
    val type: AvatarType
)

object AvatarX: AvatarModel(
    iconDrawable = R.drawable.ic_avatar_x_shadow,
    type = AvatarType.X
)

object AvatarO: AvatarModel(
    iconDrawable = R.drawable.ic_avatar_o_shadow,
    type = AvatarType.O
)