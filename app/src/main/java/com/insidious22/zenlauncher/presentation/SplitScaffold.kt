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
    Box(modifier.fillMaxSize()) {

        // Fondo general (puede ser el oscuro)
        Row(Modifier.fillMaxSize()) {
            // Panel izquierdo (claro)
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(leftRatio)
                    .background(ZenPalette.Peach),
                content = leftContent
            )

            // Panel derecho (oscuro)
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f - leftRatio)
                    .background(ZenPalette.Brown),
                content = rightContent
            )
        }

        // “Seam shadow” suave en la división para dar profundidad
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .width(18.dp)
                .offset(x = 0.dp) // se posiciona encima; HomeScreen lo alinea con Row
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(
                            Color.Black.copy(alpha = 0.22f),
                            Color.Transparent
                        )
                    )
                )
        )
    }
}
