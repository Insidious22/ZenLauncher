package com.insidious22.zenlauncher.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

private val Context.favoritesDataStore by preferencesDataStore(name = "zen_prefs")

class FavoritesStore(private val context: Context) {

    private val FAV_KEY = stringSetPreferencesKey("favorite_apps")

    val favoritesFlow: Flow<Set<String>> = context.favoritesDataStore.data
        .map { prefs -> prefs[FAV_KEY] ?: emptySet() }
        .distinctUntilChanged()

    suspend fun toggleFavorite(packageName: String) {
        context.favoritesDataStore.edit { prefs ->
            val current = prefs[FAV_KEY] ?: emptySet()
            prefs[FAV_KEY] =
                if (packageName in current) current - packageName else current + packageName
        }
    }
}
