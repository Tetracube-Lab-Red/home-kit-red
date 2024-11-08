package red.tetracube.homekitred.iot.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridItemSpan
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import androidx.navigation.NavHostController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import red.tetracube.homekitred.app.behaviour.routing.Routes
import red.tetracube.homekitred.iot.home.components.MenuBottomSheet
import red.tetracube.homekitred.iot.home.components.UPSCard
import red.tetracube.homekitred.iot.home.domain.models.HubWithRooms
import red.tetracube.homekitred.ui.core.models.UIState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IoTHomeScreen(
    viewModel: IoTHomeViewModel,
    navController: NavHostController
) {
    val uiState = viewModel.uiState
    val screenScope = rememberCoroutineScope()
    var showBottomSheet = remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = false,
    )

    LaunchedEffect(Unit) {
        viewModel.loadHubData()
    }

    val toggleBottomSheet = {
        showBottomSheet.value = !showBottomSheet.value
    }

    IoTHomeScreenUI(
        uiState = uiState.value,
        screenScope = screenScope,
        menuBottomSheet = {
            MenuBottomSheet(
                sheetState = sheetState,
                onModalDismissRequest = toggleBottomSheet,
                onAddRoomClick = {
                    toggleBottomSheet()
                    navController.navigate(Routes.RoomAdd)
                }
            )
        },
        showBottomSheet = showBottomSheet.value,
        toggleBottomSheet = toggleBottomSheet
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IoTHomeScreenUI(
    uiState: UIState,
    screenScope: CoroutineScope,
    menuBottomSheet: @Composable () -> Unit,
    showBottomSheet: Boolean,
    toggleBottomSheet: () -> Unit
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "IoT Dashboard"
                    )
                },
                actions = {
                    if (uiState is UIState.FinishedWithSuccessContent<*>) {
                        val hub = uiState.content as HubWithRooms
                        IconButton(
                            onClick = { toggleBottomSheet() }
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(CircleShape)
                                    .background(color = MaterialTheme.colorScheme.tertiaryContainer),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = hub.avatarName,
                                    color = MaterialTheme.colorScheme.onSecondary
                                )
                            }
                        }
                    } else if (uiState is UIState.Loading) {
                        CircularProgressIndicator()
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            if (uiState is UIState.Loading) {
                Popup(
                    alignment = Alignment.TopCenter,
                    properties = PopupProperties(
                        dismissOnBackPress = false,
                        dismissOnClickOutside = false,
                        clippingEnabled = true,
                    )
                ) {
                    ElevatedCard {
                        LinearProgressIndicator()
                    }
                }
            }

            if (uiState is UIState.FinishedWithSuccessContent<*>) {
                val hub = uiState.content as HubWithRooms
                val pagerState = rememberPagerState(initialPage = 0, pageCount = { hub.rooms.size })

                ScrollableTabRow(
                    selectedTabIndex = pagerState.currentPage,
                    divider = {
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    hub.rooms.mapIndexed { idx, room ->
                        Tab(
                            selected = pagerState.currentPage == idx,
                            onClick = {
                                screenScope.launch {
                                    pagerState.animateScrollToPage(idx)
                                }
                            },
                            text = {
                                Text(text = room.name)
                            }
                        )
                    }
                }
                HorizontalPager(state = pagerState) { index ->
                    DevicesGrid(index)
                }

                if (showBottomSheet) {
                    menuBottomSheet()
                }
            }
        }
    }
}

@Composable
fun DevicesGrid(index: Int) {
    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Fixed(2)
    ) {
        item(span = StaggeredGridItemSpan.FullLine) {
            UPSCard()
        }
    }
}

