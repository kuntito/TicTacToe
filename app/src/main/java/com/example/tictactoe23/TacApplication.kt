package com.example.tictactoe23

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.example.tictactoe23.data.PreferencesRepo

private const val PREFERENCES_NAME = "tac_preferences"
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    name = PREFERENCES_NAME
)

class TacApplication: Application() {
    lateinit var preferencesRepo: PreferencesRepo

    override fun onCreate() {
        super.onCreate()
        preferencesRepo = PreferencesRepo(dataStore)
    }
}