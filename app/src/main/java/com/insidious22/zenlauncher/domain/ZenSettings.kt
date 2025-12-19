package com.insidious22.zenlauncher.domain

enum class ThemeMode {
    SYSTEM, LIGHT, DARK
}

data class ZenSettings(
    val splitRatio: Float = 0.45f,          // 0.35 .. 0.55
    val showSearch: Boolean = true,
    val showAlphabet: Boolean = true,
    val appTextScale: Float = 1.0f,         // 0.85 .. 1.20
    val clockTextScale: Float = 1.0f,       // 0.90 .. 1.30
    val monochromeIcons: Boolean = false,
    val haptic: Boolean = true,
    val themeMode: ThemeMode = ThemeMode.SYSTEM,
    val appWidgetId: Int = -1
)
