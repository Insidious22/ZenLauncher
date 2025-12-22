package com.insidious22.zenlauncher.presentation

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.insidious22.zenlauncher.domain.ZenSettings
import com.insidious22.zenlauncher.ui.theme.ZenLauncherTheme
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(viewModel: HomeViewModel = viewModel()) {

    val scope = rememberCoroutineScope()
    val listState = rememberLazyListState()

    val apps by viewModel.appsFlow.collectAsState()
    val favorites by viewModel.favoritesFlow.collectAsState(initial = emptySet())
    val settings by viewModel.settingsFlow.collectAsState(initial = ZenSettings())
    val searchText by viewModel.searchQuery.collectAsState()

    var showSettings by remember { mutableStateOf(false) }

    // Mapa letra -> primer índice (sobre la lista ya filtrada)
    val letterToIndex = remember(apps) {
        buildMap<String, Int> {
            apps.forEachIndexed { index, app ->
                val c = app.label.firstOrNull()?.uppercaseChar() ?: return@forEachIndexed
                val key = c.toString()
                if (!containsKey(key)) put(key, index)
            }
        }
    }

    BackHandler(enabled = true) {
        when {
            searchText.isNotEmpty() -> viewModel.onSearchTextChange("")
            showSettings -> showSettings = false
            else -> { }
        }
    }

    ZenLauncherTheme(themeMode = settings.themeMode) {

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {

            SplitScaffold(
                leftRatio = settings.splitRatio.coerceIn(0.35f, 0.55f),
                leftContent = {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(ZenPalette.Peach)
                            .padding(24.dp)
                            .pullDownToOpen(
                                enabled = !showSettings,
                                threshold = 44.dp
                            ) { showSettings = true },
                        contentAlignment = Alignment.Center
                    ) {
                        ClockWidget(
                            clockScale = settings.clockTextScale,
                            onOpenSettings = { showSettings = true }
                        )
                    }
                },
                rightContent = {
                    val density = LocalDensity.current
                    CompositionLocalProvider(
                        LocalDensity provides Density(density.density, fontScale = settings.appTextScale)
                    ) {
                        Box(Modifier.fillMaxSize()) {

                            AppsPanel(
                                apps = apps,
                                favorites = favorites,
                                query = searchText,
                                showSearch = settings.showSearch,
                                monochromeIcons = settings.monochromeIcons,
                                listState = listState,
                                onQueryChange = viewModel::onSearchTextChange,
                                onToggleFavorite = viewModel::toggleFavorite
                            )

                            if (settings.showAlphabet && apps.isNotEmpty()) {
                                AlphabetSideBarWave(
                                    letters = ('A'..'Z').map { it.toString() },
                                    onLetterSelected = { idx ->
                                        // Busca esa letra o la siguiente disponible
                                        val targetIndex = (idx..25)
                                            .map { ('A'.code + it).toChar().toString() }
                                            .firstNotNullOfOrNull { letterToIndex[it] }

                                        if (targetIndex != null) {
                                            scope.launch { listState.animateScrollToItem(targetIndex) }
                                        }
                                    },
                                    modifier = Modifier
                                        .align(Alignment.CenterEnd)
                                        .padding(vertical = 18.dp)
                                )
                            }
                        }
                    }
                }
            )

            if (showSettings) {
                ModalBottomSheet(
                    onDismissRequest = { showSettings = false },
                    containerColor = MaterialTheme.colorScheme.surface,
                    dragHandle = null
                ) {
                    SettingsSheet(
                        settings = settings,
                        onClose = { showSettings = false },
                        onThemeModeChange = viewModel::setThemeMode,
                        onSplitRatioChange = viewModel::setSplitRatio,
                        onShowSearchChange = viewModel::setShowSearch,
                        onShowAlphabetChange = viewModel::setShowAlphabet,
                        onAppTextScaleChange = viewModel::setAppTextScale,
                        onClockTextScaleChange = viewModel::setClockTextScale,
                        onMonochromeIconsChange = viewModel::setMonochromeIcons,
                        onHapticChange = viewModel::setHaptic
                    )
                }
            }
        }
    }
}
