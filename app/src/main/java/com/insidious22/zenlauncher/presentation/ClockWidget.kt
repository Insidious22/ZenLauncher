package com.insidious22.zenlauncher.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun ClockWidget(
    clockScale: Float = 1.0f,
    onOpenSettings: () -> Unit,
) {
    var now by remember { mutableStateOf(Date()) }

    LaunchedEffect(Unit) {
        while (true) {
            now = Date()
            delay(1000)
        }
    }

    val timeFmt = remember { SimpleDateFormat("HH:mm", Locale.getDefault()) }
    val dateFmt = remember { SimpleDateFormat("EEE, dd MMM", Locale.getDefault()) }

    Column(
        modifier = Modifier.clickable { onOpenSettings() }
    ) {
        Text(
            text = timeFmt.format(now),
            fontSize = (56.sp * clockScale),
            color = ZenPalette.PeachText,
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(6.dp))
        Text(
            text = dateFmt.format(now),
            fontSize = (16.sp * clockScale),
            color = ZenPalette.PeachText.copy(alpha = 0.75f),
            textAlign = TextAlign.Center
        )
    }
}
