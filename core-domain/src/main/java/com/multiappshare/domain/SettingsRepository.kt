package com.multiappshare.domain

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class SettingsRepository(private val context: Context) {

    private val onboardingCompletedKey = booleanPreferencesKey("onboarding_completed")
    private val darkThemeKey = booleanPreferencesKey("dark_theme") // true = Dark, false = Light, null = System
    private val sharingDelayKey = androidx.datastore.preferences.core.intPreferencesKey("sharing_delay")
    private val appLanguageKey = stringPreferencesKey("app_language")
    private val crashCaptureKey = booleanPreferencesKey("crash_capture_enabled")
    private val highRefreshKey = booleanPreferencesKey("high_refresh_enabled")
    private val shareHapticsKey = booleanPreferencesKey("share_haptics_enabled")
    private val dynamicColorKey = booleanPreferencesKey("dynamic_color_enabled")
    private val pinnedGroupsKey = stringSetPreferencesKey("pinned_group_names")

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

    val isCrashCaptureEnabled: Flow<Boolean> = context.dataStore.data
        .map { preferences -> preferences[crashCaptureKey] ?: SettingsDefaults.CRASH_CAPTURE_ENABLED }

    val isHighRefreshEnabled: Flow<Boolean> = context.dataStore.data
        .map { preferences -> preferences[highRefreshKey] ?: SettingsDefaults.HIGH_REFRESH_ENABLED }

    val isShareHapticsEnabled: Flow<Boolean> = context.dataStore.data
        .map { preferences -> preferences[shareHapticsKey] ?: SettingsDefaults.SHARE_HAPTICS_ENABLED }

    val isDynamicColorEnabled: Flow<Boolean> = context.dataStore.data
        .map { preferences -> preferences[dynamicColorKey] ?: SettingsDefaults.DYNAMIC_COLOR_ENABLED }

    val pinnedGroupNames: Flow<Set<String>> = context.dataStore.data
        .map { preferences -> preferences[pinnedGroupsKey] ?: emptySet() }

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
            preferences[sharingDelayKey] = SharingDelay.clamp(delayMs)
        }
    }

    suspend fun setCrashCaptureEnabled(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[crashCaptureKey] = enabled
        }
    }

    suspend fun setHighRefreshEnabled(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[highRefreshKey] = enabled
        }
    }

    suspend fun setShareHapticsEnabled(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[shareHapticsKey] = enabled
        }
    }

    suspend fun setDynamicColorEnabled(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[dynamicColorKey] = enabled
        }
    }

    suspend fun setPinnedGroupNames(names: Set<String>) {
        context.dataStore.edit { preferences ->
            if (names.isEmpty()) preferences.remove(pinnedGroupsKey) else preferences[pinnedGroupsKey] = names
        }
    }

    val appLanguage: Flow<String?> = context.dataStore.data
        .map { preferences -> AppLanguageTags.sanitize(preferences[appLanguageKey]) }

    suspend fun snapshotSettings(): BackupSettings {
        val prefs = context.dataStore.data.first()
        return BackupSettings(
            darkTheme = prefs[darkThemeKey],
            appLanguage = AppLanguageTags.sanitize(prefs[appLanguageKey]),
            sharingDelay = prefs[sharingDelayKey],
        )
    }

    suspend fun restoreSettings(settings: BackupSettings) {
        setDarkTheme(settings.darkTheme)
        setAppLanguage(settings.appLanguage)
        settings.sharingDelay?.let { setSharingDelay(it) }
    }

    suspend fun setAppLanguage(tag: String?) {
        val sanitized = AppLanguageTags.sanitize(tag)
        context.dataStore.edit { preferences ->
            if (sanitized == null) {
                preferences.remove(appLanguageKey)
            } else {
                preferences[appLanguageKey] = sanitized
            }
        }
    }
}
