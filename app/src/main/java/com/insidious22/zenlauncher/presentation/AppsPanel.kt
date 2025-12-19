package com.insidious22.zenlauncher.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.unit.dp
import com.insidious22.zenlauncher.domain.AppModel

@Composable
fun AppsPanel(
    apps: List<AppModel>,
    favorites: Set<String>,
    query: String,
    showSearch: Boolean,
    monochromeIcons: Boolean,
    onQueryChange: (String) -> Unit,
    onToggleFavorite: (String) -> Unit,
) {
    Column(
        Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {

        if (showSearch) {
            OutlinedTextField(
                value = query,
                onValueChange = onQueryChange,
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Search") }
            )
            Spacer(Modifier.height(12.dp))
        }

        LazyColumn(Modifier.fillMaxSize()) {
            items(apps, key = { it.packageName }) { app ->
                Row(
                    Modifier
                        .fillMaxWidth()
                        .clickable { /* aquí tu open app */ }
                        .padding(vertical = 10.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    if (app.icon != null) {
                        Image(
                            bitmap = app.icon,
                            contentDescription = app.label,
                            modifier = Modifier.size(40.dp),
                            colorFilter = if (monochromeIcons) ColorFilter.tint(MaterialTheme.colorScheme.onBackground) else null
                        )
                    }

                    Column(Modifier.weight(1f)) {
                        Text(app.label, style = MaterialTheme.typography.titleMedium)
                        Text(app.packageName, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f))
                    }

                    Text(
                        text = if (favorites.contains(app.packageName)) "★" else "☆",
                        modifier = Modifier
                            .padding(horizontal = 8.dp)
                            .clickable { onToggleFavorite(app.packageName) },
                        style = MaterialTheme.typography.titleLarge
                    )
                }
            }
        }
    }
}
