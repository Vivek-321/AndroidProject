package com.example.smartroute.data.repo

import android.content.Context
import android.content.pm.ApplicationInfo
import android.os.Build
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.smartroute.data.datastore.appDataStore
import com.example.smartroute.data.model.SettingsData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class SettingsRepository(private val context: Context) {

    private object Keys {
        val VOICE_ENABLED = booleanPreferencesKey("voice_enabled")
        val ALERT_INTENSITY = stringPreferencesKey("alert_intensity")
        val THEME_MODE = stringPreferencesKey("theme_mode")
        val LAST_SETTINGS_APK_UPDATE_TIME = longPreferencesKey("last_settings_apk_update_time")
    }

    fun settingsFlow(): Flow<SettingsData> {
        return context.appDataStore.data.map { preferences ->
            SettingsData(
                voiceInstructionsEnabled = preferences[Keys.VOICE_ENABLED] ?: true,
                alertIntensity = preferences[Keys.ALERT_INTENSITY] ?: "Medium",
                themeMode = preferences[Keys.THEME_MODE] ?: "System"
            )
        }
    }

    suspend fun setVoiceInstructions(enabled: Boolean) {
        context.appDataStore.edit { preferences ->
            preferences[Keys.VOICE_ENABLED] = enabled
        }
    }

    suspend fun setAlertIntensity(value: String) {
        context.appDataStore.edit { preferences ->
            preferences[Keys.ALERT_INTENSITY] = value
        }
    }

    suspend fun setThemeMode(value: String) {
        context.appDataStore.edit { preferences ->
            preferences[Keys.THEME_MODE] = value
        }
    }

    /**
     * Reset theme to default ("System") whenever APK is newly installed/updated.
     */
    suspend fun resetThemeIfApkUpdated() {
        val packageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
        val currentApkUpdateTime = packageInfo.lastUpdateTime

        val prefs = context.appDataStore.data.first()
        val savedApkUpdateTime = prefs[Keys.LAST_SETTINGS_APK_UPDATE_TIME]

        if (savedApkUpdateTime == null || savedApkUpdateTime != currentApkUpdateTime) {
            context.appDataStore.edit { preferences ->
                // Reset only theme to default after new APK install/update
                preferences[Keys.THEME_MODE] = "System"
                preferences[Keys.LAST_SETTINGS_APK_UPDATE_TIME] = currentApkUpdateTime
            }
        }
    }

    fun getAppVersion(): String {
        val packageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
        return packageInfo.versionName ?: "1.0"
    }

    fun getBuildType(): String {
        val debuggable = (context.applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE) != 0
        return if (debuggable) "debug" else "release"
    }

    fun getAutomotiveVersion(): String = Build.VERSION.RELEASE ?: "Unknown"
}

