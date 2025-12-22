package com.insidious22.zenlauncher.presentation

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.insidious22.zenlauncher.domain.Category
import kotlin.math.*

@Composable
fun CategoryWaveBar(
    categories: List<Category>,
    selected: Category,
    onSelected: (Category) -> Unit,
    modifier: Modifier = Modifier,
    onSelectedCommit: ((Category) -> Unit)? = null
) {
    if (categories.isEmpty()) return

    var widthPx by remember { mutableStateOf(1f) }
    var dragging by remember { mutableStateOf(false) }
    var dragX by remember { mutableStateOf(0f) }

    val activeIndex = categories.indexOf(selected).coerceAtLeast(0)
    val highlightX = remember { Animatable(0f) }

    fun itemW(): Float = (widthPx / categories.size.toFloat()).coerceAtLeast(1f)

    fun indexFromX(x: Float): Int {
        val w = itemW()
        return (x / w).roundToInt().coerceIn(0, categories.size - 1)
    }

    LaunchedEffect(activeIndex, widthPx) {
        if (widthPx > 1f) {
            val target = activeIndex * itemW()
            highlightX.animateTo(
                target,
                animationSpec = spring(
                    dampingRatio = 0.8f,
                    stiffness = Spring.StiffnessMedium
                )
            )
        }
    }

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .height(52.dp)
            .clip(RoundedCornerShape(16.dp))
            .onSizeChanged { widthPx = it.width.toFloat().coerceAtLeast(1f) }
            .background(ZenPalette.Cream.copy(alpha = 0.10f))
            .pointerInput(categories) {
                var lastIdx = -1

                detectHorizontalDragGestures(
                    onDragStart = { offset ->
                        dragging = true
                        dragX = offset.x.coerceIn(0f, widthPx)
                        val idx = indexFromX(dragX)
                        lastIdx = idx
                        onSelected(categories[idx])
                    },
                    onHorizontalDrag = { change, amount ->
                        dragX = (dragX + amount).coerceIn(0f, widthPx)
                        val idx = indexFromX(dragX)

                        if (idx != lastIdx) {
                            lastIdx = idx
                            onSelected(categories[idx])
                        }
                        change.consume()
                    },
                    onDragEnd = {
                        dragging = false
                        val idx = indexFromX(dragX)
                        onSelectedCommit?.invoke(categories[idx])
                    },
                    onDragCancel = {
                        dragging = false
                        onSelected(selected)
                    }
                )
            },
        color = Color.Transparent,
        contentColor = Color.Transparent
    ) {
        Box(Modifier.fillMaxSize()) {

            val pillWidthPx = itemW()
            val pillLeftPx = if (dragging) {
                (indexFromX(dragX) * itemW())
            } else {
                highlightX.value
            }

            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(6.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .graphicsLayer { translationX = pillLeftPx }
                        .width(with(LocalDensity.current) { pillWidthPx.toDp() })
                        .clip(RoundedCornerShape(14.dp))
                        .background(ZenPalette.Cream.copy(alpha = 0.22f))
                )
            }

            Row(Modifier.fillMaxSize()) {
                val density = LocalDensity.current

                categories.forEachIndexed { i, cat ->
                    val isSelected = cat == selected

                    val center = (i * itemW()) + itemW() / 2f
                    val finger = if (dragging) dragX else highlightX.value + pillWidthPx / 2f
                    val dist = abs(finger - center)

                    val waveWidth = itemW() * 2.0f
                    val t = (1f - (dist / waveWidth)).coerceIn(0f, 1f)
                    
                    val easedT = (0.5f - 0.5f * cos(t * PI.toFloat()))

                    val scale = 1f + 0.1f * easedT
                    val translationY = with(density) { (6.dp).toPx() } * (1 - easedT)
                    val alpha = 0.6f + 0.4f * easedT
                    val rotationX = -10 * (1 - easedT)


                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = cat.label,
                            fontSize = 13.sp,
                            fontWeight = if (isSelected) FontWeight.Black else FontWeight.SemiBold,
                            color = ZenPalette.Cream.copy(alpha = alpha),
                            modifier = Modifier.graphicsLayer {
                                this.scaleX = scale
                                this.scaleY = scale
                                this.translationY = translationY
                                this.rotationX = rotationX
                            }
                        )
                    }
                }
            }
        }
    }
}
