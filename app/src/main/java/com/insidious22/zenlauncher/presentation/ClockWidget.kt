package com.insidious22.zenlauncher.presentation

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun ClockWidget(
    clockScale: Float = 1.0f,
    onOpenSettings: () -> Unit,
) {
    var time by remember { mutableStateOf(LocalTime.now()) }

    // Actualizador de tiempo cada segundo
    LaunchedEffect(Unit) {
        while (true) {
            time = LocalTime.now()
            delay(1000)
        }
    }

    val timeFmt = remember { DateTimeFormatter.ofPattern("HH:mm") }
    val dateFmt = remember { DateTimeFormatter.ofPattern("EEE, dd MMM", Locale.getDefault()) }

    // El progreso va de 0.0 a 1.0 según los segundos actuales
    val secondsProgress = time.second / 60f

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size((240.dp * clockScale))
            .clickable(
                onClick = onOpenSettings,
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            )
    ) {
        // Anillo de progreso dinámico (Como el del video)
        Canvas(modifier = Modifier.fillMaxSize().padding(10.dp)) {
            // Círculo de fondo (muy tenue)
            drawCircle(
                color = ZenPalette.Cream.copy(alpha = 0.05f),
                style = Stroke(width = 3.dp.toPx())
            )
            // Anillo que se mueve
            drawArc(
                color = ZenPalette.Cream,
                startAngle = -90f,
                sweepAngle = 360f * secondsProgress,
                useCenter = false,
                style = Stroke(width = 3.dp.toPx(), cap = StrokeCap.Round)
            )
        }

        // Hora y Fecha centradas
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = time.format(timeFmt),
                fontSize = (72.sp * clockScale),
                fontWeight = FontWeight.ExtraLight, // Estilo fino del video
                color = ZenPalette.PeachText,
                letterSpacing = (-2).sp
            )
            Text(
                text = java.time.LocalDate.now().format(dateFmt).uppercase(),
                fontSize = (12.sp * clockScale),
                fontWeight = FontWeight.Bold,
                color = ZenPalette.PeachText.copy(alpha = 0.4f),
                letterSpacing = 3.sp
            )
        }
    }
}