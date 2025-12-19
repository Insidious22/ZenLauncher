package com.insidious22.zenlauncher.presentation

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
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

    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val apps by viewModel.appsFlow.collectAsState(initial = emptyList())
    val favorites by viewModel.favoritesFlow.collectAsState(initial = emptySet())
    val settings by viewModel.settingsFlow.collectAsState(initial = ZenSettings())
    val searchText by viewModel.searchQuery.collectAsState()

    var showSettings by remember { mutableStateOf(false) }

    BackHandler(enabled = true) {
        when {
            searchText.isNotEmpty() -> viewModel.onSearchTextChange("")
            showSettings -> showSettings = false
            else -> {
                // tu lógica anterior (exit dialog / default launcher) si la tienes
            }
        }
    }

    ZenLauncherTheme(themeMode = settings.themeMode) {

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {

            SplitScaffold(
                splitRatio = settings.splitRatio.coerceIn(0.35f, 0.55f),

                left = {
                    // HEADER / CLOCK PANEL (crema)
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

                right = {
                    // APP LIST + Alphabet bar (como el video)
                    val density = LocalDensity.current
                    CompositionLocalProvider(
                        LocalDensity provides Density(density.density, fontScale = settings.appTextScale)
                    ) {

                        Box(Modifier.fillMaxSize()) {

                            // Tu panel de apps (si no tienes AppsPanel, pon tu LazyColumn aquí)
                            AppsPanel(
                                apps = apps,
                                favorites = favorites,
                                query = searchText,
                                showSearch = settings.showSearch,
                                monochromeIcons = settings.monochromeIcons,
                                onQueryChange = viewModel::onSearchTextChange,
                                onToggleFavorite = viewModel::toggleFavorite
                            )

                            if (settings.showAlphabet) {
                                AlphabetSideBarWave(
                                    letters = ('A'..'Z').map { it.toString() },
                                    onLetterSelected = { idx ->
                                        val letter = ('A'.code + idx).toChar().toString()
                                        val targetIndex = apps.indexOfFirst {
                                            it.label.startsWith(letter, ignoreCase = true)
                                        }
                                        if (targetIndex >= 0) {
                                            scope.launch {
                                                // AppsPanel debe exponer listState si quieres scroll real.
                                                // Por ahora esto te confirma que la barra funciona.
                                                // Luego te conecto el scroll real.
                                            }
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
