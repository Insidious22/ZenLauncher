package com.insidious22.zenlauncher.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
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
    background = Color(0xFF0E0E0F),
    surface = Color(0xFF0E0E0F),
    primary = Color(0xFFF2E7D6),
    onPrimary = Color(0xFF0E0E0F),
    onBackground = Color(0xFFF2E7D6),
    onSurface = Color(0xFFF2E7D6)
)

private fun zenTypography(): Typography {
    val base = Typography()
    val ff = FontFamily.SansSerif

    return base.copy(
        bodyLarge = TextStyle(
            fontFamily = ff,
            fontWeight = FontWeight.Medium,
            fontSize = 16.sp,
            letterSpacing = 0.2.sp
        ),
        bodyMedium = TextStyle(
            fontFamily = ff,
            fontWeight = FontWeight.Medium,
            fontSize = 14.sp,
            letterSpacing = 0.15.sp
        ),
        labelLarge = TextStyle(
            fontFamily = ff,
            fontWeight = FontWeight.SemiBold,
            fontSize = 12.sp,
            letterSpacing = 0.4.sp
        ),
        titleLarge = TextStyle(
            fontFamily = ff,
            fontWeight = FontWeight.SemiBold,
            fontSize = 22.sp,
            letterSpacing = 0.0.sp
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

    MaterialTheme(
        colorScheme = if (dark) DarkColors else LightColors,
        typography = zenTypography(),
        content = content
    )
}
