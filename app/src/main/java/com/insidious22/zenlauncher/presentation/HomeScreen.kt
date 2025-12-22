package com.insidious22.zenlauncher.presentation

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.insidious22.zenlauncher.domain.Category
import com.insidious22.zenlauncher.domain.ZenSettings
import com.insidious22.zenlauncher.ui.theme.ZenLauncherTheme
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(viewModel: HomeViewModel = viewModel()) {

    val scope = rememberCoroutineScope()
    val listState = rememberLazyListState()

    val apps by viewModel.filteredAppsFlow.collectAsState()
    val favorites by viewModel.favoritesFlow.collectAsState(initial = emptySet())
    val settings by viewModel.settingsFlow.collectAsState(initial = ZenSettings())
    val searchText by viewModel.searchQuery.collectAsState()
    val selectedCategory by viewModel.selectedCategory.collectAsState()

    var showSettings by remember { mutableStateOf(false) }

    val letters = remember(apps) {
        apps.mapNotNull { it.label.firstOrNull()?.uppercaseChar()?.toString() }
            .distinct()
            .sorted()
    }

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
            else -> Unit
        }
    }

    ZenLauncherTheme(themeMode = settings.themeMode) {

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(ZenPalette.BgDark) // Fondo negro total
        ) {

            val contentModifier = if (showSettings) {
                Modifier.blur(radius = 12.dp)
            } else {
                Modifier
            }

            SplitScaffold(
                modifier = contentModifier,
                leftRatio = settings.splitRatio.coerceIn(0.35f, 0.55f),

                leftContent = {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(ZenPalette.Peach) // Gris muy oscuro
                            .padding(24.dp)
                            .pullDownToOpen(
                                enabled = !showSettings,
                                threshold = 44.dp
                            ) { showSettings = true },
                        contentAlignment = Alignment.Center // Reloj centrado para el look minimalista
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
                        LocalDensity provides Density(
                            density = density.density,
                            fontScale = settings.appTextScale
                        )
                    ) {
                        Box(Modifier.fillMaxSize()) {
                            Column(Modifier.fillMaxSize()) {

                                // Barra de categorías
                                CategoryWaveBar(
                                    categories = listOf(
                                        Category.ALL,
                                        Category.FAVORITES,
                                        Category.WORK,
                                        Category.MEDIA
                                    ),
                                    selected = selectedCategory,
                                    onSelected = { /* Animación visual */ },
                                    onSelectedCommit = { viewModel.setCategory(it) },
                                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 14.dp)
                                )

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
                            }

                            if (settings.showAlphabet && letters.isNotEmpty()) {
                                AlphabetSideBarWave(
                                    letters = letters,
                                    onLetterSelected = { idx ->
                                        val letter = letters.getOrNull(idx) ?: return@AlphabetSideBarWave
                                        val targetIndex = letterToIndex[letter] ?: return@AlphabetSideBarWave
                                        scope.launch { listState.animateScrollToItem(targetIndex) }
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