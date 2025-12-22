package com.insidious22.zenlauncher.presentation

import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

fun Modifier.pullDownToOpen(
    enabled: Boolean = true,
    threshold: Dp = 44.dp,
    onTriggered: () -> Unit
): Modifier = composed {
    if (!enabled) return@composed this

    val thresholdPx = with(LocalDensity.current) { threshold.toPx() }

    pointerInput(Unit) {
        var accumulated = 0f
        detectVerticalDragGestures(
            onDragStart = { accumulated = 0f },
            onVerticalDrag = { change, dragAmount ->
                if (dragAmount > 0f) accumulated += dragAmount

                if (accumulated >= thresholdPx) {
                    accumulated = 0f
                    change.consume()
                    onTriggered()
                } else {
                    change.consume()
                }
            }
        )
    }
}
