package com.tito.tictactoe.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.compose.runtime.Composable
import androidx.fragment.app.FragmentManager
import androidx.navigation.fragment.findNavController
import com.tito.tictactoe.models.GameMode
import com.tito.tictactoe.models.viewmodel.GameViewModel
import com.tito.tictactoe.ui.composables.PlayScreen
import com.tito.tictactoe.ui.theme.TicTacToeTheme

private lateinit var viewModel: GameViewModel
private lateinit var fragManager: FragmentManager
var currentGameMode: GameMode? = null
class PlayFragment : BaseFragment() {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = gameViewModel
        fragManager = parentFragmentManager

        gameViewModel.boards.forEach {
            it.resetBoard()
        }
        binding.composeView.setContent {
            TicTacToeTheme {
                PlayScreen(
                    gameViewModel = gameViewModel,
                    navController = findNavController(),
                    fragmentManager = fragManager
                )
            }
        }
    }
}
