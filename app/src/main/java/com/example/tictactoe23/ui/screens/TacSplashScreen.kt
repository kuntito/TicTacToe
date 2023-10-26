package com.example.tictactoe23.ui.screens

import android.view.animation.OvershootInterpolator
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.tictactoe23.Destinations
import com.example.tictactoe23.R
import kotlinx.coroutines.delay

@Composable
fun TacSplashScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
) {
    val scale = remember {
        Animatable(0f)
    }

    LaunchedEffect(key1 = true) {
        scale.animateTo(
            targetValue = 1f,
            animationSpec = tween(
                durationMillis = 500,
                easing = {
                    OvershootInterpolator(2f).getInterpolation(it)
                }
            )
        )
        delay(2000L)
        navController.navigate(Destinations.MainScreenDst.scr_name) {
            // Prevents back button from going back to splash screen
            popUpTo(0)
        }

    }
    val surfaceShape = RoundedCornerShape(32.dp)
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Surface(
            shape = surfaceShape,
            modifier = modifier
                .shadow(5.dp, shape = surfaceShape)
        ) {
            Image(
                painter = painterResource(id = R.drawable.tac_splash_logo),
                contentDescription = "app_logo",
                modifier = Modifier
                    .scale(scale.value)
            )
        }
    }
}