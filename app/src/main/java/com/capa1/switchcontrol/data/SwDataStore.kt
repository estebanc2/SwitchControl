package com.capa1.switchcontrol.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.map
import javax.inject.Inject

val Context.dataStore by preferencesDataStore(name = "DS")

class SwDataStore @Inject constructor (
    private val context: Context
) {
    suspend fun saveFlashData(data: String) {
        context.dataStore.edit { preferences ->
            preferences[stringPreferencesKey("SWITCHES")] = data
        }
    }
    fun getFlashData() = context.dataStore.data.map {
        it[stringPreferencesKey("SWITCHES")].orEmpty()
    }
}