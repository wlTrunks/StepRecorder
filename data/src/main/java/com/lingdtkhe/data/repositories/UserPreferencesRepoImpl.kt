package com.lingdtkhe.data.repositories

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.lingdtkhe.domain.repository.UserPreferencesRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.withContext

class UserPreferencesRepoImpl(
    private val context: Context
) : UserPreferencesRepo {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
        name = "user_preferences"
    )

    override suspend fun saveStartRecordTime(time: Long) {
        withContext(Dispatchers.IO) {
            context.dataStore.edit { preferences ->
                preferences[KEY_GAMES_INVITES_ENABLED] = time
            }
        }
    }

    override suspend fun getLastRecordTime(): Long? = withContext(Dispatchers.IO) {
        context.dataStore.data.firstOrNull()?.get(KEY_GAMES_INVITES_ENABLED)
    }

    companion object {
        private val KEY_GAMES_INVITES_ENABLED = longPreferencesKey("key_last_record_time")
    }
}