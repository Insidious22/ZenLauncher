package com.insidious22.zenlauncher.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.insidious22.zenlauncher.domain.AppModel
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.cos

@Composable
fun AppsPanel(
    apps: List<AppModel>,
    interactionY: Float?,
    listState: LazyListState,
    favorites: Set<String>,
    query: String,
    showSearch: Boolean,
    monochromeIcons: Boolean,
    searchHint: String,
    onQueryChange: (String) -> Unit,
    onToggleFavorite: (String) -> Unit,
    onLaunchApp: (String) -> Unit
) {
    val tint = remember { ColorFilter.tint(ZenPalette.DeepBlack, BlendMode.SrcAtop) }

    Column(Modifier.fillMaxSize()) {
        if (showSearch) {
            OutlinedTextField(
                value = query,
                onValueChange = onQueryChange,
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 30.dp, vertical = 8.dp),
                label = { Text(searchHint) },
                shape = MaterialTheme.shapes.medium
            )
        }

        LazyColumn(
            state = listState,
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(top = 10.dp, bottom = 250.dp)
        ) {
            itemsIndexed(apps, key = { _, app -> app.packageName }) { _, app ->
                var itemY by remember { mutableStateOf(0f) }
                val dist = interactionY?.let { abs(it - itemY) } ?: Float.MAX_VALUE
                val proximity = (1f - (dist / AnimationConstants.APPS_WAVE_RANGE_PX)).coerceIn(0f, 1f)
                val wave = (0.5f - 0.5f * cos(proximity * PI.toFloat()))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .onGloballyPositioned { itemY = it.positionInWindow().y + (it.size.height / 2) }
                        .graphicsLayer {
                            translationX = -(wave * AnimationConstants.APPS_WAVE_TRANSLATION_DP).dp.toPx()
                            alpha = 0.5f + (0.5f * proximity)
                        }
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                            onClick = { onLaunchApp(app.packageName) }
                        )
                        .padding(vertical = 10.dp, horizontal = 45.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (app.icon != null) {
                        Box(
                            modifier = Modifier
                                .size(30.dp)
                                .clip(CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Image(
                                bitmap = app.icon,
                                contentDescription = null,
                                modifier = Modifier.fillMaxSize(),
                                colorFilter = if (monochromeIcons) tint else null
                            )
                        }
                        Spacer(Modifier.width(18.dp))
                    }
                    Text(
                        text = app.label,
                        modifier = Modifier.weight(1f),
                        color = ZenPalette.DeepBlack,
                        fontSize = 16.sp,
                        fontWeight = if (wave > 0.5f) FontWeight.Bold else FontWeight.Normal,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    Text(
                        text = if (favorites.contains(app.packageName)) "★" else "☆",
                        color = ZenPalette.DeepBlack.copy(alpha = if (wave > 0.5f) 1f else 0.2f),
                        modifier = Modifier
                            .clickable { onToggleFavorite(app.packageName) }
                            .padding(8.dp),
                        fontSize = 18.sp
                    )
                }
            }
        }
    }
}