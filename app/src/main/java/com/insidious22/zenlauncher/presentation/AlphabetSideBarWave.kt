package com.insidious22.zenlauncher.presentation

import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.pow

@Composable
fun AlphabetSideBarWave(
    letters: List<String>,
    onInteraction: (Float?) -> Unit,
    onLetterSelected: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    var size by remember { mutableStateOf(IntSize.Zero) }
    var globalYOffset by remember { mutableStateOf(0f) }
    var fingerY by remember { mutableStateOf<Float?>(null) }

    Box(
        modifier = modifier
            .width(80.dp)
            .fillMaxHeight()
            .onGloballyPositioned { globalYOffset = it.positionInWindow().y }
            .onSizeChanged { size = it }
            .pointerInput(letters) {
                detectVerticalDragGestures(
                    onDragStart = { offset ->
                        fingerY = offset.y
                        onInteraction(offset.y + globalYOffset)
                        onLetterSelected((offset.y / (size.height / letters.size.toFloat())).toInt().coerceIn(0, letters.size - 1))
                    },
                    onVerticalDrag = { change, _ ->
                        fingerY = change.position.y
                        onInteraction(change.position.y + globalYOffset)
                        onLetterSelected((change.position.y / (size.height / letters.size.toFloat())).toInt().coerceIn(0, letters.size - 1))
                    },
                    onDragEnd = {
                        fingerY = null
                        onInteraction(null)
                    },
                    onDragCancel = {
                        fingerY = null
                        onInteraction(null)
                    }
                )
            },
        contentAlignment = Alignment.CenterEnd
    ) {
        Column(
            Modifier.fillMaxHeight(),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.End
        ) {
            letters.forEachIndexed { i, ch ->
                val itemH = if (size.height > 0) size.height / letters.size.toFloat() else 1f
                val centerY = (i * itemH) + itemH / 2f
                val dist = fingerY?.let { abs(it - centerY) } ?: Float.MAX_VALUE
                val t = (1f - (dist / 280f)).coerceIn(0f, 1f)
                val wave = (0.5f - 0.5f * cos(t * PI.toFloat())).pow(1.8f)

                Text(
                    text = ch,
                    fontSize = 11.sp,
                    fontWeight = if (wave > 0.5f) FontWeight.Black else FontWeight.Normal,
                    color = ZenPalette.DeepBlack.copy(alpha = 0.3f + (0.7f * wave)),
                    modifier = Modifier
                        .padding(end = 12.dp)
                        .graphicsLayer {
                            scaleX = 1f + (2.5f * wave)
                            scaleY = 1f + (2.5f * wave)
                            translationX = -(wave * 90f).dp.toPx()
                        }
                )
            }
        }
    }
}