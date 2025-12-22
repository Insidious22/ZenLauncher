package com.insidious22.zenlauncher.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun SplitScaffold(
    modifier: Modifier = Modifier,
    leftRatio: Float = 0.42f,
    leftContent: @Composable ColumnScope.() -> Unit,
    rightContent: @Composable ColumnScope.() -> Unit
) {
    Row(modifier.fillMaxSize()) {

        Column(
            modifier = Modifier
                .fillMaxHeight()
                .weight(leftRatio)
                .background(ZenPalette.Peach),
            content = leftContent
        )

        // Seam shadow (queda justo en la división, sin offsets raros)
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .width(14.dp)
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(
                            Color.Black.copy(alpha = 0.18f),
                            Color.Transparent
                        )
                    )
                )
        )

        Column(
            modifier = Modifier
                .fillMaxHeight()
                .weight(1f - leftRatio)
                .background(ZenPalette.Brown),
            content = rightContent
        )
    }
}
