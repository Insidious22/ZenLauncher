package com.insidious22.zenlauncher.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.cos

@Composable
fun AlphabetSideBarWave(
    letters: List<String>,
    onLetterSelected: (index: Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    if (letters.isEmpty()) return

    var size by remember { mutableStateOf(IntSize.Zero) }
    var fingerY by remember { mutableStateOf<Float?>(null) }
    var lastSelectedIndex by remember { mutableStateOf(-1) }

    fun indexFromY(y: Float): Int {
        val h = size.height.toFloat().coerceAtLeast(1f)
        val itemH = h / letters.size.toFloat().coerceAtLeast(1f)
        return (y / itemH).toInt().coerceIn(0, letters.size - 1)
    }

    val density = LocalDensity.current

    Box(
        modifier = modifier
            .width(52.dp)
            .fillMaxHeight()
            .padding(end = 10.dp)
            .clip(RoundedCornerShape(999.dp))
            .background(MaterialTheme.colorScheme.onBackground.copy(alpha = 0.06f))
            .onSizeChanged { size = it }
            .pointerInput(letters) {
                detectVerticalDragGestures(
                    onDragStart = { offset ->
                        fingerY = offset.y
                        val index = indexFromY(offset.y)
                        if (index != lastSelectedIndex) {
                            onLetterSelected(index)
                            lastSelectedIndex = index
                        }
                    },
                    onVerticalDrag = { change, _ ->
                        fingerY = change.position.y
                        val index = indexFromY(change.position.y)
                        if (index != lastSelectedIndex) {
                            onLetterSelected(index)
                            lastSelectedIndex = index
                        }
                        change.consume()
                    },
                    onDragEnd = { fingerY = null },
                    onDragCancel = { fingerY = null }
                )
            }
    ) {
        Column(
            Modifier
                .fillMaxSize()
                .padding(vertical = 10.dp),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            letters.forEachIndexed { i, ch ->
                val centerY = if (size.height == 0) 0f else {
                    val itemH = size.height / letters.size.toFloat()
                    (i * itemH) + itemH / 2f
                }

                val dist = fingerY?.let { abs(it - centerY) } ?: Float.MAX_VALUE
                
                val waveWidth = size.height * 0.3f
                val t = (1f - (dist / waveWidth)).coerceIn(0f, 1f)
                val easedT = (0.5f - 0.5f * cos(t * PI.toFloat()))

                val scale = 1f + 0.4f * easedT
                val translationX = with(density) { (-12).dp.toPx() } * easedT
                val alpha = 0.5f + 0.5f * easedT

                Text(
                    text = ch,
                    fontSize = 11.sp,
                    color = ZenPalette.Cream, // Use full color, adjust with alpha
                    modifier = Modifier.graphicsLayer {
                        this.scaleX = scale
                        this.scaleY = scale
                        this.translationX = translationX
                        this.alpha = alpha
                    }
                )
            }
        }
    }
}
