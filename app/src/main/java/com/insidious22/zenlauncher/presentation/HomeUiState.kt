package com.insidious22.zenlauncher.presentation

import com.insidious22.zenlauncher.domain.AppModel
import com.insidious22.zenlauncher.domain.Category
import com.insidious22.zenlauncher.domain.ZenSettings

data class HomeUiState(
    val apps: List<AppModel> = emptyList(),
    val favorites: Set<String> = emptySet(),
    val selectedCategory: Category = Category.ALL,
    val settings: ZenSettings = ZenSettings(),
    val isLoading: Boolean = true
)
