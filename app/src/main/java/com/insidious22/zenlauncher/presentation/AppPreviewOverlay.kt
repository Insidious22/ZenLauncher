package com.insidious22.zenlauncher.presentation

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.insidious22.zenlauncher.R
import com.insidious22.zenlauncher.domain.AppModel

@Composable
fun AppPreviewOverlay(
    app: AppModel?,
    visible: Boolean,
    monochromeIcons: Boolean = false,
    modifier: Modifier = Modifier
) {
    val transition = updateTransition(targetState = visible && app != null, label = "preview")

    val alpha by transition.animateFloat(
        transitionSpec = { tween(durationMillis = 200) },
        label = "alpha"
    ) { if (it) 1f else 0f }

    val scale by transition.animateFloat(
        transitionSpec = { spring(dampingRatio = Spring.DampingRatioMediumBouncy) },
        label = "scale"
    ) { if (it) 1f else 0.8f }

    val blurRadius by transition.animateDp(
        transitionSpec = { tween(durationMillis = 150) },
        label = "blur"
    ) { if (it) 0.dp else 10.dp }

    if (alpha > 0f && app != null) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .alpha(alpha)
                .scale(scale),
            contentAlignment = Alignment.Center
        ) {
            // Blurred background with gradient
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Black.copy(alpha = 0.7f),
                                Color.Black.copy(alpha = 0.85f),
                                Color.Black.copy(alpha = 0.7f)
                            )
                        )
                    )
            )

            // Content card
            Column(
                modifier = Modifier
                    .fillMaxWidth(0.85f)
                    .clip(RoundedCornerShape(24.dp))
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                ZenPalette.DarkPeach.copy(alpha = 0.3f),
                                ZenPalette.DeepBlack.copy(alpha = 0.4f)
                            )
                        )
                    )
                    .blur(blurRadius)
                    .padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Large app icon
                app.icon?.let { icon ->
                    AppIconLarge(
                        icon = icon,
                        monochrome = monochromeIcons,
                        tintColor = Color.White,
                        backgroundColor = Color.White.copy(alpha = 0.1f)
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // App name
                Text(
                    text = app.label,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Package name (subtle)
                Text(
                    text = app.packageName,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color.White.copy(alpha = 0.5f),
                    textAlign = TextAlign.Center,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Action hint
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.White.copy(alpha = 0.15f))
                        .padding(horizontal = 20.dp, vertical = 10.dp)
                ) {
                    Text(
                        text = stringResource(R.string.tap_to_launch),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.White.copy(alpha = 0.8f)
                    )
                }
            }
        }
    }
}
