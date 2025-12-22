package com.insidious22.zenlauncher.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun ClockWidget(
    clockScale: Float = 1.0f,
    onOpenSettings: () -> Unit
) {
    val time = LocalTime.now()

    Column(
        modifier = Modifier
            .padding(start = 45.dp, top = 40.dp)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onOpenSettings
            ),
        horizontalAlignment = Alignment.Start
    ) {
        Box(
            modifier = Modifier
                .width(1.5.dp)
                .height(140.dp)
                .background(ZenPalette.DeepBlack)
        )
        Text("▼", fontSize = 10.sp, color = ZenPalette.DeepBlack, modifier = Modifier.offset(x = (-4).dp, y = (-4).dp))

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "It's ${time.format(DateTimeFormatter.ofPattern("hh:mm a"))}",
            fontSize = (20.sp * clockScale),
            fontWeight = FontWeight.Black,
            color = ZenPalette.DeepBlack,
            letterSpacing = (-1).sp
        )
        Text(
            text = java.time.LocalDate.now().format(DateTimeFormatter.ofPattern("EEEE, d MMM", Locale.ENGLISH)).uppercase(),
            fontSize = (11.sp * clockScale),
            fontWeight = FontWeight.Bold,
            color = ZenPalette.DeepBlack.copy(alpha = 0.6f),
            letterSpacing = 1.sp
        )
    }
}