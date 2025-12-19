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
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.insidious22.zenlauncher.domain.Category
import kotlin.math.abs
import kotlin.math.roundToInt

@Composable
fun CategoryWaveBar(
    categories: List<Category>,
    selected: Category,
    onSelected: (Category) -> Unit,
    modifier: Modifier = Modifier
) {
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
        val target = activeIndex * itemW()
        highlightX.animateTo(
            target,
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioNoBouncy,
                stiffness = Spring.StiffnessMediumLow
            )
        )
    }

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .height(52.dp)
            .clip(RoundedCornerShape(16.dp))
            .onSizeChanged { widthPx = it.width.toFloat().coerceAtLeast(1f) }
            .background(ZenPalette.Cream.copy(alpha = 0.10f))
            .pointerInput(categories, selected) {
                detectHorizontalDragGestures(
                    onDragStart = { offset ->
                        dragging = true
                        dragX = offset.x
                        onSelected(categories[indexFromX(dragX)])
                    },
                    onHorizontalDrag = { change, amount ->
                        dragX = (dragX + amount).coerceIn(0f, widthPx)
                        onSelected(categories[indexFromX(dragX)])
                        change.consume()
                    },
                    onDragEnd = { dragging = false },
                    onDragCancel = { dragging = false }
                )
            },
        color = androidx.compose.ui.graphics.Color.Transparent,
        contentColor = androidx.compose.ui.graphics.Color.Transparent
    ) {
        Box(Modifier.fillMaxSize()) {

            // Highlight pill
            val pillLeftPx = if (dragging) indexFromX(dragX) * itemW() else highlightX.value
            val pillLeftDp = (pillLeftPx / widthPx) * (/*52dp height irrelevant*/ 0f)

            // No usamos conversion px->dp aquí: usamos offset via padding con pesos + Spacer.
            // Para mantenerlo simple y estable: hacemos “pill” con Row de pesos.
            Row(Modifier.fillMaxSize().padding(6.dp)) {
                val leftWeight = (pillLeftPx / itemW()).coerceIn(0f, categories.size.toFloat())
                Spacer(Modifier.weight(leftWeight))
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(1f)
                        .clip(RoundedCornerShape(14.dp))
                        .background(ZenPalette.Cream.copy(alpha = 0.22f))
                )
                Spacer(Modifier.weight((categories.size - 1f - leftWeight).coerceAtLeast(0f)))
            }

            // Labels
            Row(Modifier.fillMaxSize()) {
                categories.forEachIndexed { i, cat ->
                    val isSelected = cat == selected

                    val center = (i * itemW()) + itemW() / 2f
                    val finger = if (dragging) dragX else center
                    val dist = abs(finger - center)
                    val t = (1f - (dist / (itemW() * 1.4f))).coerceIn(0f, 1f)
                    val scale = 1f + 0.06f * t

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
                            color = ZenPalette.Cream.copy(alpha = if (isSelected) 0.95f else 0.65f),
                            modifier = Modifier.graphicsLayer {
                                scaleX = scale
                                scaleY = scale
                            }
                        )
                    }
                }
            }
        }
    }
}
