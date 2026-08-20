package com.multiappshare.domain

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.first
import kotlinx.serialization.SerializationException
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class ShareSessionStore(private val context: Context) {

    private val inflightKey = stringPreferencesKey("share_inflight")
    private val lastPayloadKey = stringPreferencesKey("share_last_payload")
    private val json = Json { ignoreUnknownKeys = true }

    suspend fun saveInflight(snapshot: ShareSessionSnapshot) {
        write(inflightKey, snapshot)
    }

    suspend fun clearInflight() {
        context.dataStore.edit { it.remove(inflightKey) }
    }

    suspend fun loadInflight(): ShareSessionSnapshot? = read(inflightKey)

    suspend fun saveLastPayload(snapshot: ShareSessionSnapshot) {
        write(lastPayloadKey, snapshot.copy(appPackages = emptyList(), currentIndex = 0, sharingStarted = false))
    }

    suspend fun loadLastPayload(): ShareSessionSnapshot? = read(lastPayloadKey)

    private suspend fun write(key: Preferences.Key<String>, snapshot: ShareSessionSnapshot) {
        context.dataStore.edit { prefs ->
            prefs[key] = json.encodeToString(snapshot)
        }
    }

    private suspend fun read(key: Preferences.Key<String>): ShareSessionSnapshot? {
        val raw = context.dataStore.data.first()[key] ?: return null
        return try {
            json.decodeFromString<ShareSessionSnapshot>(raw)
        } catch (_: SerializationException) {
            null
        }
    }
}
