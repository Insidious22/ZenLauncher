package com.insidious22.zenlauncher.presentation

import android.appwidget.AppWidgetHost
import android.appwidget.AppWidgetManager
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView

@Composable
fun ZenAppWidget(
    host: AppWidgetHost,
    appWidgetId: Int,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val manager = remember { AppWidgetManager.getInstance(context) }

    val info = remember(appWidgetId) { manager.getAppWidgetInfo(appWidgetId) }

    Surface(
        modifier = modifier
            .clip(RoundedCornerShape(18.dp)),
        color = ZenPalette.Cream.copy(alpha = 0.20f)
    ) {
        if (info == null || appWidgetId == -1) {
            Box(
                Modifier
                    .fillMaxSize()
                    .padding(14.dp)
            ) {
                Text(
                    text = "No widget",
                    color = ZenPalette.PeachText,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        } else {
            AndroidView(
                modifier = Modifier.fillMaxSize(),
                factory = {
                    val hostView = host.createView(it, appWidgetId, info)
                    hostView.setAppWidget(appWidgetId, info)
                    hostView.layoutParams = FrameLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
                    hostView
                },
                update = { view ->
                    // opcional: puedes ajustar opciones/tamaño aquí si quieres
                    view.setPadding(0, 0, 0, 0)
                }
            )
        }
    }
}
