package com.insidious22.zenlauncher.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
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
 * When monochrome is enabled, the icon is converted to grayscale and tinted
 * with the specified color, with a subtle background for consistency.
 */
@Composable
fun AppIcon(
    icon: ImageBitmap?,
    monochrome: Boolean,
    modifier: Modifier = Modifier,
    size: Dp = IconConstants.ICON_SIZE_SMALL,
    tintColor: Color = ZenPalette.DeepBlack,
    backgroundColor: Color = ZenPalette.DarkPeach.copy(alpha = 0.3f)
) {
    if (icon == null) return

    val colorFilter = if (monochrome) {
        // Grayscale matrix + tint for true monochrome effect
        ColorFilter.colorMatrix(
            ColorMatrix().apply {
                setToSaturation(0f)
            }
        )
    } else null

    val tintFilter = if (monochrome) {
        ColorFilter.tint(tintColor, BlendMode.SrcIn)
    } else null

    Box(
        modifier = modifier
            .size(size)
            .clip(RoundedCornerShape(IconConstants.ICON_CORNER_RADIUS))
            .then(
                if (monochrome) {
                    Modifier.background(backgroundColor)
                } else Modifier
            ),
        contentAlignment = Alignment.Center
    ) {
        if (monochrome) {
            // For monochrome: draw grayscale version first, then tinted overlay
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(IconConstants.ICON_PADDING),
                contentAlignment = Alignment.Center
            ) {
                // Base layer - extract shape with solid color
                Image(
                    bitmap = icon,
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Fit,
                    colorFilter = ColorFilter.tint(tintColor, BlendMode.SrcIn)
                )
            }
        } else {
            // Normal colored icon
            Image(
                bitmap = icon,
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Fit
            )
        }
    }
}

/**
 * Large app icon variant for preview overlays
 */
@Composable
fun AppIconLarge(
    icon: ImageBitmap?,
    monochrome: Boolean,
    modifier: Modifier = Modifier,
    tintColor: Color = Color.White,
    backgroundColor: Color = Color.White.copy(alpha = 0.1f)
) {
    AppIcon(
        icon = icon,
        monochrome = monochrome,
        modifier = modifier,
        size = IconConstants.ICON_SIZE_LARGE,
        tintColor = tintColor,
        backgroundColor = backgroundColor
    )
}
