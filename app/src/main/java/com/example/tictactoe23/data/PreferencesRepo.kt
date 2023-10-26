package com.example.tictactoe23.data

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.tictactoe23.models.PlayerHuman
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException


const val CURRENT_PLAYER_NAME = "current_player_name"
class PreferencesRepo (
    private val dataStore: DataStore<Preferences>
) {
    private companion object {
        val CURRENT_PLAYER_NAME_KEY = stringPreferencesKey(CURRENT_PLAYER_NAME)
        const val TAG = "PreferencesRepo"
    }

    suspend fun saveCurrentPlayer(playerName: String) {
        dataStore.edit {preferences ->
            preferences[CURRENT_PLAYER_NAME_KEY] = playerName
        }
    }

    val currentPlayerName: Flow<String> = dataStore.data
        .catch {
            if (it is IOException) {
                Log.e(TAG, "Error reading preferences", it)
                emit(emptyPreferences())
            } else {
                throw it
            }
        }
        .map{ it[CURRENT_PLAYER_NAME_KEY] ?: PlayerHuman.label.name }
}