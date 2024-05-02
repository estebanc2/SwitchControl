package com.capa1.switchcontrol.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SwDataStore(private val context: Context) {
    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("SW_DATA")
        private val KEY = stringPreferencesKey("SWITCHES")
    }
    val getFlashData: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[KEY] ?: ""
    }
    suspend fun saveFlashData(data: String) {
        context.dataStore.edit { preferences ->
            preferences[KEY] = data
        }
    }
}