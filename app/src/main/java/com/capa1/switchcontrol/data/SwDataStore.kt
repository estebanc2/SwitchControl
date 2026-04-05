package com.capa1.switchcontrol.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

val Context.dataStore by preferencesDataStore(name = "DS")

class SwDataStore @Inject constructor (
    private val context: Context
) {

    companion object {
        val MY_STRING_KEY = stringPreferencesKey("my_string")
    }

    suspend fun saveFlashData(data: String) {
        context.dataStore.edit { prefs ->
            prefs[MY_STRING_KEY] = data
        }
    }
    val myFlashData: Flow<String> = context.dataStore.data.map {
        prefs -> prefs[MY_STRING_KEY] ?:""
    }
}