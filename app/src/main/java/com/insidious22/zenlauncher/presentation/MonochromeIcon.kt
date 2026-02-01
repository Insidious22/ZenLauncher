package com.insidious22.zenlauncher.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Constants for icon styling
 */
object IconConstants {
    val ICON_SIZE_SMALL = 32.dp
    val ICON_SIZE_MEDIUM = 48.dp
    val ICON_SIZE_LARGE = 120.dp
    val ICON_CORNER_RADIUS = 8.dp
    val ICON_PADDING = 4.dp
}

/**
 * A composable that renders an app icon with optional monochrome styling.
 * When monochrome is enabled, the icon is converted to grayscale (desaturated).
 */
@Composable
fun AppIcon(
    icon: ImageBitmap?,
    monochrome: Boolean,
    modifier: Modifier = Modifier,
    size: Dp = IconConstants.ICON_SIZE_SMALL
) {
    if (icon == null) return

    // Grayscale filter for true monochrome effect
    val colorFilter = if (monochrome) {
        ColorFilter.colorMatrix(
            ColorMatrix().apply {
                setToSaturation(0f)
            }
        )
    } else null

    Box(
        modifier = modifier.size(size),
        contentAlignment = Alignment.Center
    ) {
        Image(
            bitmap = icon,
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Fit,
            colorFilter = colorFilter
        )
    }
}

/**
 * Large app icon variant for preview overlays
 */
@Composable
fun AppIconLarge(
    icon: ImageBitmap?,
    monochrome: Boolean,
    modifier: Modifier = Modifier
) {
    AppIcon(
        icon = icon,
        monochrome = monochrome,
        modifier = modifier,
        size = IconConstants.ICON_SIZE_LARGE
    )
}
