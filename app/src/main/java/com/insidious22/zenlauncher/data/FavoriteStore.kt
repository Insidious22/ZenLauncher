package com.insidious22.zenlauncher.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

val Context.dataStore by preferencesDataStore(name = "zen_prefs")

class FavoritesStore(private val context: Context) {
    private val FAV_KEY = stringSetPreferencesKey("favorite_apps")

    val favoritesFlow: Flow<Set<String>> = context.dataStore.data
        .map { preferences -> preferences[FAV_KEY] ?: emptySet() }
        .distinctUntilChanged()

    suspend fun toggleFavorite(packageName: String) {
        context.dataStore.edit { prefs ->
            val current = prefs[FAV_KEY] ?: emptySet()
            prefs[FAV_KEY] = if (packageName in current) current - packageName else current + packageName
        }
    }
}
