package com.insidious22.zenlauncher.presentation

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.insidious22.zenlauncher.R
import com.insidious22.zenlauncher.domain.AppModel
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

    var interactionY by remember { mutableStateOf<Float?>(null) }
    var showSettings by remember { mutableStateOf(false) }
    var highlightedApp by remember { mutableStateOf<AppModel?>(null) }
    var isInteracting by remember { mutableStateOf(false) }
    val pagerState = rememberPagerState(pageCount = { 3 })

    BackHandler(enabled = showSettings || searchText.isNotEmpty()) {
        if (showSettings) {
            showSettings = false
        } else {
            viewModel.onSearchTextChange("")
        }
    }

    ZenLauncherTheme(themeMode = settings.themeMode) {
        Box(modifier = Modifier.fillMaxSize()) {

            Row(Modifier.fillMaxSize()) {
                Box(Modifier.weight(0.58f).fillMaxHeight().background(ZenPalette.LightPeach))
                Box(Modifier.weight(0.42f).fillMaxHeight().background(ZenPalette.DarkPeach))
            }

            val contentModifier = if (showSettings) Modifier.blur(20.dp) else Modifier

            Column(modifier = contentModifier.fillMaxSize().statusBarsPadding()) {

                Box(modifier = Modifier.weight(0.35f)) {
                    HorizontalPager(state = pagerState, modifier = Modifier.fillMaxSize()) { page ->
                        if (page == 0) {
                            ClockWidget(
                                clockScale = settings.clockTextScale,
                                onOpenSettings = { showSettings = true }
                            )
                        } else {
                            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                Text(
                                    stringResource(R.string.swipe_to_return),
                                    color = ZenPalette.DeepBlack.copy(0.2f)
                                )
                            }
                        }
                    }
                }

                Box(modifier = Modifier.weight(0.65f)) {

                    AppsPanel(
                        apps = apps,
                        interactionY = interactionY,
                        listState = listState,
                        favorites = favorites,
                        query = searchText,
                        showSearch = settings.showSearch,
                        monochromeIcons = settings.monochromeIcons,
                        searchHint = stringResource(R.string.search_hint),
                        onQueryChange = viewModel::onSearchTextChange,
                        onToggleFavorite = viewModel::toggleFavorite,
                        onLaunchApp = viewModel::launchApp
                    )

                    val letters = remember(apps) {
                        apps.map { it.label.firstOrNull()?.uppercase() ?: "" }
                            .distinct()
                            .sorted()
                    }

                    AlphabetSideBarWave(
                        letters = letters,
                        onInteraction = { y ->
                            interactionY = y
                            isInteracting = y != null
                            if (y == null) {
                                highlightedApp = null
                            }
                        },
                        onLetterSelected = { index ->
                            scope.launch {
                                val char = letters.getOrNull(index) ?: return@launch
                                val appIndex = apps.indexOfFirst { it.label.startsWith(char, ignoreCase = true) }
                                if (appIndex != -1) {
                                    listState.scrollToItem(appIndex)
                                    highlightedApp = apps.getOrNull(appIndex)
                                }
                            }
                        },
                        modifier = Modifier.align(Alignment.CenterEnd)
                    )
                }
            }

            if (showSettings) {
                ModalBottomSheet(
                    onDismissRequest = { showSettings = false },
                    dragHandle = null,
                    containerColor = ZenPalette.DeepBlack,
                    scrimColor = Color.Black.copy(alpha = 0.5f)
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

            // App preview overlay (console-style)
            AppPreviewOverlay(
                app = highlightedApp,
                visible = isInteracting && highlightedApp != null
            )
        }
    }
}