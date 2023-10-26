package com.example.tictactoe23


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.tictactoe23.ui.screens.MainScreen
import com.example.tictactoe23.ui.screens.TacSplashScreen
import com.example.tictactoe23.ui.theme.TicTacToe23Theme
import com.example.tictactoe23.ui.theme.color100

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val viewModel: TacViewModel = viewModel(
                factory = TacViewModel.Factory
            )
            TicTacToe23Theme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = color100
                ) {
                    Navigation(viewModel = viewModel)
                }
            }
        }
    }
}

enum class Destinations(val scr_name: String) {
    SplashScreenDst("splash"),
    MainScreenDst("main")
}

@Composable
fun Navigation(
    modifier: Modifier = Modifier,
    viewModel: TacViewModel
) {
    val startDestination = Destinations.SplashScreenDst
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = startDestination.scr_name,
        modifier = modifier
    ) {
        composable(Destinations.SplashScreenDst.scr_name) {
            TacSplashScreen(
                navController = navController
            )
        }
        composable(Destinations.MainScreenDst.scr_name) {
            MainScreen(viewModel = viewModel)
        }
    }
}

