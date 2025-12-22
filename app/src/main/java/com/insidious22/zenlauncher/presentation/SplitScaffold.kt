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
    Row(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .weight(leftRatio)
                .background(ZenPalette.LightPeach),
            content = leftContent
        )

        Box(
            modifier = Modifier
                .fillMaxHeight()
                .width(1.dp)
                .background(Color.Black.copy(alpha = 0.1f))
        )

        Column(
            modifier = Modifier
                .fillMaxHeight()
                .weight(1f - leftRatio)
                .background(ZenPalette.DarkPeach),
            content = rightContent
        )
    }
}