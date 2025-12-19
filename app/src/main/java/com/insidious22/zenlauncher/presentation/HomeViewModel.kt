package com.insidious22.zenlauncher.presentation

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.insidious22.zenlauncher.data.AppsRepository
import com.insidious22.zenlauncher.data.FavoriteStore
import com.insidious22.zenlauncher.data.SettingsStore
import com.insidious22.zenlauncher.domain.ThemeMode
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    private val appsRepo = AppsRepository(context = application)
    private val favoritesStore = FavoriteStore(application)
    private val settingsStore = SettingsStore(context = application)

    // Flows “directos” (HomeScreen usa collectAsState(initial=...))
    val appsFlow = appsRepo.appsFlow
    val favoritesFlow = favoritesStore.favoritesFlow
    val settingsFlow = settingsStore.settingsFlow

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    fun onSearchTextChange(text: String) {
        _searchQuery.value = text
    }

    fun toggleFavorite(packageName: String) = viewModelScope.launch {
        favoritesStore.toggle(packageName)
    }

    // ---- Settings setters (persisten offline) ----
    fun setSplitRatio(value: Float) = viewModelScope.launch { settingsStore.setSplitRatio(value) }
    fun setShowSearch(value: Boolean) = viewModelScope.launch { settingsStore.setShowSearch(value) }
    fun setShowAlphabet(value: Boolean) = viewModelScope.launch { settingsStore.setShowAlphabet(value) }
    fun setAppTextScale(value: Float) = viewModelScope.launch { settingsStore.setAppTextScale(value) }
    fun setClockTextScale(value: Float) = viewModelScope.launch { settingsStore.setClockTextScale(value) }
    fun setMonochromeIcons(value: Boolean) = viewModelScope.launch { settingsStore.setMonochromeIcons(value) }
    fun setHaptic(value: Boolean) = viewModelScope.launch { settingsStore.setHaptic(value) }
    fun setThemeMode(value: ThemeMode) = viewModelScope.launch { settingsStore.setThemeMode(value) }
}
