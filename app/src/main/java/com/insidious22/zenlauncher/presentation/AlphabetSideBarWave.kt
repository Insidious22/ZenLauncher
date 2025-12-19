package com.insidious22.zenlauncher.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp

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
        val itemH = h / letters.size.toFloat().coerceAtLeast(1f)
        return (y / itemH).toInt().coerceIn(0, letters.size - 1)
    }

    Box(
        modifier = modifier
            .width(52.dp)
            .fillMaxHeight()
            .padding(end = 8.dp)
            .clip(RoundedCornerShape(999.dp))
            .background(MaterialTheme.colorScheme.onBackground.copy(alpha = 0.06f))
            .onSizeChanged { size = it }
            .pointerInput(Unit) {
                detectVerticalDragGestures(
                    onDragStart = { offset ->
                        fingerY = offset.y
                        onLetterSelected(indexFromY(offset.y))
                    },
                    onVerticalDrag = { change, _ ->
                        fingerY = change.position.y
                        onLetterSelected(indexFromY(change.position.y))
                    },
                    onDragEnd = { fingerY = null },
                    onDragCancel = { fingerY = null }
                )
            }
    )
}
