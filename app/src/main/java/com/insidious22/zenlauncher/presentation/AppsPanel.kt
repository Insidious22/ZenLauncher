package com.insidious22.zenlauncher.presentation

import android.content.Context
import android.content.Intent
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.insidious22.zenlauncher.domain.AppModel

@Composable
fun AppsPanel(
    apps: List<AppModel>,
    favorites: Set<String>,
    query: String,
    showSearch: Boolean,
    monochromeIcons: Boolean,
    listState: LazyListState,
    onQueryChange: (String) -> Unit,
    onToggleFavorite: (String) -> Unit,
) {
    val context = LocalContext.current
    val tint = remember {
        ColorFilter.tint(ZenPalette.Cream.copy(alpha = 0.92f), BlendMode.SrcIn)
    }

    Column(
        Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 16.dp)
    ) {
        if (showSearch) {
            Spacer(Modifier.height(16.dp))
            OutlinedTextField(
                value = query,
                onValueChange = onQueryChange,
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Search") }
            )
            Spacer(Modifier.height(12.dp))
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            state = listState,
            contentPadding = PaddingValues(top = 16.dp, bottom = 80.dp)
        ) {
            items(
                items = apps,
                key = { it.packageName }
            ) { app ->

                val animatable = remember(app.packageName) { Animatable(0f) }

                LaunchedEffect(app.packageName) {
                    animatable.animateTo(
                        targetValue = 1f,
                        animationSpec = spring(dampingRatio = 0.8f, stiffness = 200f)
                    )
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .animateItem()
                        .graphicsLayer {
                            alpha = animatable.value
                            translationY = (1 - animatable.value) * 20.dp.toPx()
                        }
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Click en el app (Sin Ripple para evitar el crash)
                    Row(
                        modifier = Modifier
                            .weight(1f)
                            .noRippleClickable { openApp(context, app.packageName) },
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        if (app.icon != null) {
                            Image(
                                bitmap = app.icon,
                                contentDescription = app.label,
                                modifier = Modifier.size(44.dp),
                                colorFilter = if (monochromeIcons) tint else null
                            )
                        }

                        Text(
                            text = app.label,
                            style = MaterialTheme.typography.titleMedium,
                            color = ZenPalette.Cream
                        )
                    }

                    // Click en Favorito (Sin Ripple para evitar el crash)
                    Text(
                        text = if (favorites.contains(app.packageName)) "★" else "☆",
                        modifier = Modifier
                            .padding(8.dp)
                            .noRippleClickable { onToggleFavorite(app.packageName) },
                        style = MaterialTheme.typography.headlineSmall,
                        color = ZenPalette.Cream.copy(alpha = 0.8f)
                    )
                }
            }
        }
    }
}

// EXTENSIÓN PARA CLICK SIN RIPPLE (Solución al Crash de Compose 1.7+)
fun Modifier.noRippleClickable(onClick: () -> Unit): Modifier = composed {
    this.clickable(
        interactionSource = remember { MutableInteractionSource() },
        indication = null, // En 1.7+, esto solo funciona si no hay conflictos de librerías
        onClick = onClick
    )
}

private fun openApp(context: Context, packageName: String) {
    try {
        val pm = context.packageManager
        val intent = pm.getLaunchIntentForPackage(packageName)?.apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        if (intent != null) context.startActivity(intent)
    } catch (e: Exception) {
        e.printStackTrace()
    }
}