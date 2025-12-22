package com.insidious22.zenlauncher.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import com.insidious22.zenlauncher.domain.ThemeMode

private val LightColors = lightColorScheme(
    background = Color(0xFFF6F1E7),
    surface = Color(0xFFF6F1E7),
    primary = Color(0xFF121212),
    onPrimary = Color.White,
    onBackground = Color(0xFF121212),
    onSurface = Color(0xFF121212)
)

private val DarkColors = darkColorScheme(
    background = Color(0xFF000000),
    surface = Color(0xFF000000),
    primary = Color(0xFFFFFFFF),
    onPrimary = Color(0xFF000000),
    onBackground = Color(0xFFF2E7D6),
    onSurface = Color(0xFFF2E7D6)
)

private fun zenTypography(): Typography {
    val ff = FontFamily.SansSerif
    return Typography(
        bodyLarge = TextStyle(
            fontFamily = ff,
            fontWeight = FontWeight.Medium,
            fontSize = 16.sp,
            letterSpacing = 0.2.sp
        ),
        titleLarge = TextStyle(
            fontFamily = ff,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 22.sp,
            letterSpacing = (-1).sp
        )
    )
}

@Composable
fun ZenLauncherTheme(
    themeMode: ThemeMode,
    content: @Composable () -> Unit
) {
    val dark = when (themeMode) {
        ThemeMode.SYSTEM -> isSystemInDarkTheme()
        ThemeMode.DARK -> true
        ThemeMode.LIGHT -> false
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = Color.Transparent.toArgb()
            window.navigationBarColor = Color.Transparent.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !dark
        }
    }

    MaterialTheme(
        colorScheme = if (dark) DarkColors else LightColors,
        typography = zenTypography(),
        content = content
    )
}