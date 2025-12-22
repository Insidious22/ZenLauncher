package com.insidious22.zenlauncher.domain

enum class ThemeMode {
    SYSTEM, LIGHT, DARK
}

data class ZenSettings(
    val splitRatio: Float = 0.45f,
    val showSearch: Boolean = true,
    val showAlphabet: Boolean = true,
    val appTextScale: Float = 1.0f,
    val clockTextScale: Float = 1.0f,
    val monochromeIcons: Boolean = false,
    val haptic: Boolean = true,
    val themeMode: ThemeMode = ThemeMode.SYSTEM,
    val appWidgetId: Int = -1
)
