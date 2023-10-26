package com.example.tictactoe23.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.tictactoe23.models.AvatarModel
import com.example.tictactoe23.models.AvatarO
import com.example.tictactoe23.models.AvatarX
import com.example.tictactoe23.ui.components.general.Icon
import com.example.tictactoe23.ui.components.general.preview.PreviewColumn

@Composable
fun AvatarIcon(
    modifier: Modifier = Modifier,
    size: Int,
    model: AvatarModel
) {
    Icon(
        size = size,
        iconDrawable = model.iconDrawable,
        contentDescription = "avatar ${model.type.name}",
        modifier = modifier
    )
}

@Preview
@Composable
fun AvatarPreview() {
    val size = 48
    PreviewColumn {
        AvatarIcon(
            size = size,
            model = AvatarX
        )
        AvatarIcon(
            size = size,
            model = AvatarO
        )
    }
}