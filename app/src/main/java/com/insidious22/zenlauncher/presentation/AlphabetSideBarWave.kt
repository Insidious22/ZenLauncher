package com.insidious22.zenlauncher.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.abs
import kotlin.math.exp

@Composable
fun AlphabetSideBarWave(
    letters: List<String>,
    onLetterSelected: (index: Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    var size by remember { mutableStateOf(IntSize.Zero) }
    var fingerY by remember { mutableStateOf<Float?>(null) }

    fun indexFromY(y: Float): Int {
        val h = size.height.toFloat().coerceAtLeast(1f)
        val itemH = (h / letters.size.toFloat()).coerceAtLeast(1f)
        return (y / itemH).toInt().coerceIn(0, letters.size - 1)
    }

    Box(
        modifier = modifier
            .width(52.dp)
            .fillMaxHeight()
            .padding(end = 8.dp)
            .clip(RoundedCornerShape(999.dp))
            .background(MaterialTheme.colorScheme.onBackground.copy(alpha = 0.08f))
            .onSizeChanged { size = it }
            .pointerInput(letters) {
                detectVerticalDragGestures(
                    onDragStart = { offset ->
                        fingerY = offset.y
                        onLetterSelected(indexFromY(offset.y))
                    },
                    onVerticalDrag = { change, _ ->
                        fingerY = change.position.y
                        onLetterSelected(indexFromY(change.position.y))
                        change.consume()
                    },
                    onDragEnd = { fingerY = null },
                    onDragCancel = { fingerY = null }
                )
            }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 10.dp),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val h = size.height.toFloat().coerceAtLeast(1f)
            val itemH = (h / letters.size.toFloat()).coerceAtLeast(1f)

            letters.forEachIndexed { i, ch ->
                val centerY = (i + 0.5f) * itemH
                val dy = fingerY?.let { abs(it - centerY) } ?: 99999f

                val t = exp(-(dy / (itemH * 1.6f)))
                val scale = 1f + 0.28f * t
                val alpha = 0.45f + 0.55f * t

                androidx.compose.material3.Text(
                    text = ch,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = alpha),
                    modifier = Modifier.graphicsLayer {
                        scaleX = scale
                        scaleY = scale
                    }
                )
            }
        }
    }
}
