package com.multiappshare.domain

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class SettingsRepository(private val context: Context) {

    private val onboardingCompletedKey = booleanPreferencesKey("onboarding_completed")
    private val darkThemeKey = booleanPreferencesKey("dark_theme") // true = Dark, false = Light, null = System
    private val sharingDelayKey = androidx.datastore.preferences.core.intPreferencesKey("sharing_delay")

    val isOnboardingCompleted: Flow<Boolean> = context.dataStore.data
        .map { preferences ->
            preferences[onboardingCompletedKey] ?: false
        }

    val isDarkThemeEnabled: Flow<Boolean?> = context.dataStore.data
        .map { preferences ->
            preferences[darkThemeKey]
        }

    val sharingDelay: Flow<Int> = context.dataStore.data
        .map { preferences ->
            preferences[sharingDelayKey] ?: 500
        }

    suspend fun setOnboardingCompleted() {
        context.dataStore.edit { preferences ->
            preferences[onboardingCompletedKey] = true
        }
    }

    suspend fun setDarkTheme(enabled: Boolean?) {
        context.dataStore.edit { preferences ->
            if (enabled != null) {
                preferences[darkThemeKey] = enabled
            } else {
                preferences.remove(darkThemeKey)
            }
        }
    }

    suspend fun setSharingDelay(delayMs: Int) {
        context.dataStore.edit { preferences ->
            preferences[sharingDelayKey] = delayMs
        }
    }
}
