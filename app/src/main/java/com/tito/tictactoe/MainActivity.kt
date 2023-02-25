package com.tito.tictactoe

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import com.tito.tictactoe.databinding.ActivityMainBinding
import com.tito.tictactoe.models.viewmodel.GameViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

private const val mainActivityTag = "mainActivity"
const val pref_name = "pref"
const val currentBoardIndexKey = "currentBoardIndexKey"
const val currentPlayerOrdinalKey = "currentPlayerOrdinalKey"
class MainActivity : AppCompatActivity() {
    private val gameViewModel: GameViewModel by viewModels()
    private lateinit var binding: ActivityMainBinding
    private lateinit var sharedPref: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sharedPref = getSharedPreferences(pref_name, Context.MODE_PRIVATE)
        sharedPref.getInt(currentBoardIndexKey, 0).let{
            Log.d(mainActivityTag, "loaded boardIndex=$it")
            gameViewModel.setCurrentBoardIndex(it)
        }
        sharedPref.getInt(currentPlayerOrdinalKey, 1).let{
            Log.d(mainActivityTag, "loaded playerOrdinal=$it")
            gameViewModel.setCurrentPlayerOrdinal(it)
        }
    }

    override fun onStop() {
        super.onStop()
        val boardIndex = gameViewModel.currentBoardIndex.value!!
        val playerOrdinal = gameViewModel.currentPlayer.value!!.ordinal
        sharedPref.edit().apply {
            putInt(
                currentBoardIndexKey,
                boardIndex
            )
            putInt(
                currentPlayerOrdinalKey,
                playerOrdinal
            )
        }.apply()
        Log.d(mainActivityTag, "saved boardIndex=$boardIndex")
        Log.d(mainActivityTag, "saved playerOrdinal=$playerOrdinal")
    }
}