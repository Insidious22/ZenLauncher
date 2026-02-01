package com.insidious22.zenlauncher.presentation

import android.app.Application
import android.content.Intent
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.insidious22.zenlauncher.data.AppsRepository
import com.insidious22.zenlauncher.data.CategoryClassifier
import com.insidious22.zenlauncher.data.FavoritesDataStore
import com.insidious22.zenlauncher.data.SettingsDataStore
import com.insidious22.zenlauncher.domain.AppModel
import com.insidious22.zenlauncher.domain.Category
import com.insidious22.zenlauncher.domain.ThemeMode
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    private val appsRepo = AppsRepository(context = application)
    private val favoritesStore = FavoritesDataStore(application)
    private val settingsStore = SettingsDataStore(context = application)
    private val classifier = CategoryClassifier()

    private val _allApps = MutableStateFlow<List<AppModel>>(emptyList())
    val favoritesFlow = favoritesStore.favoritesFlow
    val settingsFlow = settingsStore.settingsFlow

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedCategory = MutableStateFlow(Category.ALL)
    val selectedCategory: StateFlow<Category> = _selectedCategory.asStateFlow()

    private val _categoryMap = MutableStateFlow<Map<String, Set<Category>>>(emptyMap())

    val filteredAppsFlow: StateFlow<List<AppModel>> =
        combine(_allApps, favoritesFlow, _searchQuery, _selectedCategory, _categoryMap) { apps, favs, query, cat, catMap ->

            val base = when (cat) {
                Category.ALL -> apps
                Category.FAVORITES -> apps.filter { it.packageName in favs }
                Category.WORK -> apps.filter { catMap[it.packageName]?.contains(Category.WORK) == true }
                Category.MEDIA -> apps.filter { catMap[it.packageName]?.contains(Category.MEDIA) == true }
            }

            val q = query.trim()
            if (q.isBlank()) base
            else base.filter { it.label.contains(q, ignoreCase = true) }

        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    init {
        viewModelScope.launch {
            val apps = appsRepo.getInstalledApps()
            _allApps.value = apps

            val map = buildMap<String, Set<Category>> {
                apps.forEach { put(it.packageName, classifier.categoryFor(it)) }
            }
            _categoryMap.value = map
        }
    }

    fun onSearchTextChange(text: String) {
        _searchQuery.value = text
    }

    fun setCategory(category: Category) {
        _selectedCategory.value = category
    }

    fun toggleFavorite(packageName: String) = viewModelScope.launch {
        favoritesStore.toggleFavorite(packageName)
    }

    fun setSplitRatio(value: Float) = viewModelScope.launch { settingsStore.setSplitRatio(value) }
    fun setShowSearch(value: Boolean) = viewModelScope.launch { settingsStore.setShowSearch(value) }
    fun setShowAlphabet(value: Boolean) = viewModelScope.launch { settingsStore.setShowAlphabet(value) }
    fun setAppTextScale(value: Float) = viewModelScope.launch { settingsStore.setAppTextScale(value) }
    fun setClockTextScale(value: Float) = viewModelScope.launch { settingsStore.setClockTextScale(value) }
    fun setMonochromeIcons(value: Boolean) = viewModelScope.launch { settingsStore.setMonochromeIcons(value) }
    fun setHaptic(value: Boolean) = viewModelScope.launch { settingsStore.setHaptic(value) }
    fun setThemeMode(value: ThemeMode) = viewModelScope.launch { settingsStore.setThemeMode(value) }

    fun launchApp(packageName: String) {
        val context = getApplication<Application>()
        val intent = context.packageManager.getLaunchIntentForPackage(packageName)
        intent?.let {
            it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(it)
        }
    }
}
