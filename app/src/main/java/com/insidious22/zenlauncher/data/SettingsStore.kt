package com.insidious22.zenlauncher.data

import android.content.Context
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.insidious22.zenlauncher.domain.ThemeMode
import com.insidious22.zenlauncher.domain.ZenSettings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "zen_settings")

class SettingsStore(private val context: Context) {

    private object Keys {
        val SPLIT_RATIO = floatPreferencesKey("split_ratio")
        val SHOW_SEARCH = booleanPreferencesKey("show_search")
        val SHOW_ALPHABET = booleanPreferencesKey("show_alphabet")
        val APP_TEXT_SCALE = floatPreferencesKey("app_text_scale")
        val CLOCK_TEXT_SCALE = floatPreferencesKey("clock_text_scale")
        val MONOCHROME_ICONS = booleanPreferencesKey("monochrome_icons")
        val HAPTIC = booleanPreferencesKey("haptic")
        val THEME_MODE = stringPreferencesKey("theme_mode")
        val APPWIDGET_ID = intPreferencesKey("appwidget_id")
    }

    val settingsFlow: Flow<ZenSettings> = context.dataStore.data.map { prefs ->
        val mode = runCatching {
            ThemeMode.valueOf(prefs[Keys.THEME_MODE] ?: ThemeMode.SYSTEM.name)
        }.getOrElse { ThemeMode.SYSTEM }

        ZenSettings(
            splitRatio = prefs[Keys.SPLIT_RATIO] ?: 0.45f,
            showSearch = prefs[Keys.SHOW_SEARCH] ?: true,
            showAlphabet = prefs[Keys.SHOW_ALPHABET] ?: true,
            appTextScale = prefs[Keys.APP_TEXT_SCALE] ?: 1.0f,
            clockTextScale = prefs[Keys.CLOCK_TEXT_SCALE] ?: 1.0f,
            monochromeIcons = prefs[Keys.MONOCHROME_ICONS] ?: false,
            haptic = prefs[Keys.HAPTIC] ?: true,
            themeMode = mode,
            appWidgetId = prefs[Keys.APPWIDGET_ID] ?: -1
        )
    }

    suspend fun setSplitRatio(value: Float) {
        context.dataStore.edit { it[Keys.SPLIT_RATIO] = value }
    }

    suspend fun setShowSearch(value: Boolean) {
        context.dataStore.edit { it[Keys.SHOW_SEARCH] = value }
    }

    suspend fun setShowAlphabet(value: Boolean) {
        context.dataStore.edit { it[Keys.SHOW_ALPHABET] = value }
    }

    suspend fun setAppTextScale(value: Float) {
        context.dataStore.edit { it[Keys.APP_TEXT_SCALE] = value }
    }

    suspend fun setClockTextScale(value: Float) {
        context.dataStore.edit { it[Keys.CLOCK_TEXT_SCALE] = value }
    }

    suspend fun setMonochromeIcons(value: Boolean) {
        context.dataStore.edit { it[Keys.MONOCHROME_ICONS] = value }
    }

    suspend fun setHaptic(value: Boolean) {
        context.dataStore.edit { it[Keys.HAPTIC] = value }
    }

    suspend fun setThemeMode(value: ThemeMode) {
        context.dataStore.edit { it[Keys.THEME_MODE] = value.name }
    }

    suspend fun setAppWidgetId(value: Int) {
        context.dataStore.edit { it[Keys.APPWIDGET_ID] = value }
    }
}
