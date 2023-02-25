package com.tito.tictactoe.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.tito.tictactoe.models.viewmodel.GameViewModel
import com.tito.tictactoe.ui.composables.HomeScreen
import com.tito.tictactoe.ui.theme.TicTacToeTheme


const val appTag = "tictactoe"
lateinit var navController: NavController
private lateinit var viewModel: GameViewModel
class HomeScreenFragment: BaseFragment(){
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = findNavController()
        viewModel = gameViewModel
        binding.composeView.setContent {
            TicTacToeTheme {
                HomeScreen(
                    gameViewModel = viewModel,
                    navController = findNavController()
                )
            }
        }
    }
}
