package com.insidious22.zenlauncher.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.insidious22.zenlauncher.domain.ThemeMode
import com.insidious22.zenlauncher.domain.ZenSettings

@Composable
fun SettingsSheet(
    settings: ZenSettings,
    onClose: () -> Unit,
    onSplitRatioChange: (Float) -> Unit,
    onShowSearchChange: (Boolean) -> Unit,
    onShowAlphabetChange: (Boolean) -> Unit,
    onAppTextScaleChange: (Float) -> Unit,
    onClockTextScaleChange: (Float) -> Unit,
    onMonochromeIconsChange: (Boolean) -> Unit,
    onHapticChange: (Boolean) -> Unit,
    onThemeModeChange: (ThemeMode) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 18.dp, vertical = 14.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Row(
            Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "Settings",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.weight(1f)
            )
            TextButton(onClick = onClose) { Text("Done") }
        }

        Spacer(Modifier.height(14.dp))
        SectionTitle("Theme")
        ThemeChips(selected = settings.themeMode, onSelect = onThemeModeChange)

        Spacer(Modifier.height(18.dp))
        SectionTitle("Layout")
        LabeledSlider(
            label = "Split ratio",
            value = settings.splitRatio,
            valueRange = 0.35f..0.55f,
            steps = 0,
            onValueChange = onSplitRatioChange
        )

        Spacer(Modifier.height(18.dp))
        SectionTitle("Visibility")
        SwitchRow("Show search", settings.showSearch, onShowSearchChange)
        SwitchRow("Alphabet sidebar", settings.showAlphabet, onShowAlphabetChange)

        Spacer(Modifier.height(18.dp))
        SectionTitle("Typography")
        LabeledSlider(
            label = "App text scale",
            value = settings.appTextScale,
            valueRange = 0.85f..1.20f,
            steps = 0,
            onValueChange = onAppTextScaleChange
        )
        LabeledSlider(
            label = "Clock text scale",
            value = settings.clockTextScale,
            valueRange = 0.90f..1.30f,
            steps = 0,
            onValueChange = onClockTextScaleChange
        )

        Spacer(Modifier.height(18.dp))
        SectionTitle("Style")
        SwitchRow("Monochrome icons", settings.monochromeIcons, onMonochromeIconsChange)
        SwitchRow("Haptic feedback", settings.haptic, onHapticChange)

        Spacer(Modifier.height(10.dp))
    }
}

@Composable
private fun SectionTitle(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.labelLarge,
        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
        modifier = Modifier.padding(bottom = 8.dp)
    )
}

@Composable
private fun SwitchRow(
    title: String,
    value: Boolean,
    onValueChange: (Boolean) -> Unit
) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(title, modifier = Modifier.weight(1f))
        Switch(checked = value, onCheckedChange = onValueChange)
    }
}

@Composable
private fun LabeledSlider(
    label: String,
    value: Float,
    valueRange: ClosedFloatingPointRange<Float>,
    steps: Int,
    onValueChange: (Float) -> Unit
) {
    Column(Modifier.fillMaxWidth()) {
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Text(label, modifier = Modifier.weight(1f))
            Text(String.format("%.2f", value), color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f))
        }
        Slider(
            value = value,
            onValueChange = onValueChange,
            valueRange = valueRange,
            steps = steps
        )
    }
}

@Composable
private fun ThemeChips(
    selected: ThemeMode,
    onSelect: (ThemeMode) -> Unit
) {
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        FilterChip(
            selected = selected == ThemeMode.SYSTEM,
            onClick = { onSelect(ThemeMode.SYSTEM) },
            label = { Text("System") }
        )
        FilterChip(
            selected = selected == ThemeMode.LIGHT,
            onClick = { onSelect(ThemeMode.LIGHT) },
            label = { Text("Light") }
        )
        FilterChip(
            selected = selected == ThemeMode.DARK,
            onClick = { onSelect(ThemeMode.DARK) },
            label = { Text("Dark") }
        )
    }
}
