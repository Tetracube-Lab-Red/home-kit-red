package red.tetracube.homekitred.iot.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavHostController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import red.tetracube.homekitred.app.models.UIState
import red.tetracube.homekitred.data.enumerations.DeviceType
import red.tetracube.homekitred.iot.home.components.MenuBottomSheet
import red.tetracube.homekitred.iot.home.components.UPSCard
import red.tetracube.homekitred.iot.home.domain.models.BasicTelemetry
import red.tetracube.homekitred.iot.home.domain.models.BottomSheetItem
import red.tetracube.homekitred.iot.home.domain.models.Device
import red.tetracube.homekitred.iot.home.domain.models.HubWithRooms
import red.tetracube.homekitred.iot.home.domain.models.deviceMenuItems
import red.tetracube.homekitred.iot.home.domain.models.globalMenuItems

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IoTHomeScreen(
    viewModel: IoTHomeViewModel,
    navController: NavHostController,
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
) {
    val uiState = viewModel.uiState
    val devicesTelemetriesMap = viewModel.devicesTelemetriesMap
    val screenScope = rememberCoroutineScope()
    var showBottomSheet = remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = false,
    )
    val toggleBottomSheet = {
        showBottomSheet.value = !showBottomSheet.value
    }

    val menuItems = remember { mutableListOf<BottomSheetItem>() }
    val globalMenuItemsBuilder: () -> Unit = {
        menuItems.clear()
        menuItems.addAll(globalMenuItems(navController, toggleBottomSheet))
        toggleBottomSheet()
    }
    val deviceMenuItemsBuilder: (String) -> Unit = { deviceSlug ->
        menuItems.clear()
        menuItems.addAll(deviceMenuItems(navController, deviceSlug, toggleBottomSheet))
        toggleBottomSheet()
    }

    LaunchedEffect(Unit) {
        viewModel.loadHubData()
    }

    DisposableEffect(lifecycleOwner) {
        onDispose {
            viewModel.cancelListeners()
        }
    }

    IoTHomeScreenUI(
        uiState = uiState.value,
        screenScope = screenScope,
        menuBottomSheet = {
            MenuBottomSheet(
                sheetState = sheetState,
                onModalDismissRequest = toggleBottomSheet,
                menuItems = menuItems
            )
        },
        showBottomSheet = showBottomSheet.value,
        onHubAvatarClick = globalMenuItemsBuilder,
        devicesTelemetriesMap = devicesTelemetriesMap.map { t -> t.value }.toMap(),
        onDeviceMenuRequest = deviceMenuItemsBuilder
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IoTHomeScreenUI(
    uiState: UIState,
    screenScope: CoroutineScope,
    menuBottomSheet: @Composable () -> Unit,
    showBottomSheet: Boolean,
    onHubAvatarClick: () -> Unit,
    devicesTelemetriesMap: Map<Device, BasicTelemetry>,
    onDeviceMenuRequest: (String) -> Unit
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
                            onClick = { onHubAvatarClick() }
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
                LinearProgressIndicator()
            }

            if (uiState is UIState.FinishedWithSuccessContent<*>) {
                val hub = uiState.content as HubWithRooms
                val pagerState = rememberPagerState(initialPage = 0, pageCount = { hub.rooms.size })
                val selectedRoomSlug = remember {
                    derivedStateOf {
                        hub.rooms[pagerState.currentPage].slug
                    }
                }
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
                    DevicesGrid(selectedRoomSlug.value, devicesTelemetriesMap, onDeviceMenuRequest)
                }
            }
        }

        if (showBottomSheet) {
            menuBottomSheet()
        }
    }
}

@Composable
fun DevicesGrid(
    roomSlug: String,
    devicesTelemetriesMap: Map<Device, BasicTelemetry>,
    onDeviceMenuRequest: (String) -> Unit
) {
    LazyColumn {
        devicesTelemetriesMap
            .filter { room ->
                roomSlug == "all" || room.key.roomSlug == roomSlug
            }
            .map { deviceTelemetryEntry ->
                item {
                    when (deviceTelemetryEntry.key.type) {
                        DeviceType.UPS -> UPSCard(
                            deviceTelemetryEntry.key,
                            deviceTelemetryEntry.value,
                            onDeviceMenuRequest
                        )

                        DeviceType.SWITCH -> TODO()
                        DeviceType.HUE -> TODO()
                        else -> {}
                    }
                }
            }
    }
}

