package com.insidious22.zenlauncher

import com.insidious22.zenlauncher.domain.ThemeMode
import com.insidious22.zenlauncher.domain.ZenSettings
import org.junit.Assert.*
import org.junit.Test

class ZenSettingsTest {

    @Test
    fun `default settings have correct values`() {
        val settings = ZenSettings()

        assertEquals(0.45f, settings.splitRatio, 0.001f)
        assertTrue(settings.showSearch)
        assertTrue(settings.showAlphabet)
        assertEquals(1.0f, settings.appTextScale, 0.001f)
        assertEquals(1.0f, settings.clockTextScale, 0.001f)
        assertFalse(settings.monochromeIcons)
        assertTrue(settings.haptic)
        assertEquals(ThemeMode.SYSTEM, settings.themeMode)
        assertEquals(-1, settings.appWidgetId)
    }

    @Test
    fun `custom settings are applied correctly`() {
        val settings = ZenSettings(
            splitRatio = 0.55f,
            showSearch = false,
            showAlphabet = false,
            appTextScale = 1.2f,
            clockTextScale = 1.1f,
            monochromeIcons = true,
            haptic = false,
            themeMode = ThemeMode.DARK,
            appWidgetId = 42
        )

        assertEquals(0.55f, settings.splitRatio, 0.001f)
        assertFalse(settings.showSearch)
        assertFalse(settings.showAlphabet)
        assertEquals(1.2f, settings.appTextScale, 0.001f)
        assertEquals(1.1f, settings.clockTextScale, 0.001f)
        assertTrue(settings.monochromeIcons)
        assertFalse(settings.haptic)
        assertEquals(ThemeMode.DARK, settings.themeMode)
        assertEquals(42, settings.appWidgetId)
    }

    @Test
    fun `copy with single change works correctly`() {
        val original = ZenSettings()
        val modified = original.copy(themeMode = ThemeMode.LIGHT)

        assertEquals(ThemeMode.SYSTEM, original.themeMode)
        assertEquals(ThemeMode.LIGHT, modified.themeMode)
        assertEquals(original.splitRatio, modified.splitRatio, 0.001f)
    }

    @Test
    fun `ThemeMode enum has all expected values`() {
        val values = ThemeMode.values()
        assertEquals(3, values.size)
        assertTrue(values.contains(ThemeMode.SYSTEM))
        assertTrue(values.contains(ThemeMode.LIGHT))
        assertTrue(values.contains(ThemeMode.DARK))
    }
}
