package red.tetracube.homekitred.ui.iot.home

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffold
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import red.tetracube.homekitred.business.enumerations.DeviceType
import red.tetracube.homekitred.ui.iot.home.components.MenuBottomSheet
import red.tetracube.homekitred.ui.iot.home.components.UPSCard
import red.tetracube.homekitred.ui.iot.home.models.Device
import red.tetracube.homekitred.ui.iot.home.models.IoTDashboardModel
import red.tetracube.homekitred.ui.iot.home.models.globalMenuItems
import red.tetracube.homekitred.ui.state.UIState
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IoTHomeScreen(
    viewModel: IoTHomeViewModel,
    navController: NavHostController,
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
) {
    val iotDataFlow = remember { viewModel.iotData() }.collectAsStateWithLifecycle(UIState.Loading)
    val screenScope = rememberCoroutineScope()

    var showBottomSheet = remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = false)
    val toggleBottomSheet: () -> Unit = {
        if (sheetState.isVisible) {
            screenScope.launch { sheetState.hide() }
        } else {
            screenScope.launch { sheetState.show() }
        }.invokeOnCompletion {
            if (sheetState.isVisible) showBottomSheet.value = true
            if (!sheetState.isVisible) showBottomSheet.value = false
        }
    }
    val menuItems = remember { globalMenuItems(navController, toggleBottomSheet) }

    LaunchedEffect(Unit) {
        viewModel.loadHubData()
    }

    DisposableEffect(lifecycleOwner) {
        onDispose {
            viewModel.cancelListeners()
        }
    }

    IotHomeScreenUINew(
        iotData = iotDataFlow.value,
        onHubAvatarClick = toggleBottomSheet,
        screenScope = screenScope,
        showBottomSheet = showBottomSheet.value,
        menuBottomSheet = {
            MenuBottomSheet(
                sheetState = sheetState,
                onModalDismissRequest = {
                    screenScope.launch { sheetState.hide() }
                        .invokeOnCompletion {
                            if (!sheetState.isVisible) showBottomSheet.value = false
                        }
                },
                menuItems = menuItems
            )
        }
    )
}

@OptIn(ExperimentalMaterial3AdaptiveApi::class, ExperimentalMaterial3Api::class)
@Composable
fun IotHomeScreenUINew(
    screenScope: CoroutineScope,
    iotData: UIState,
    onHubAvatarClick: () -> Unit,
    menuBottomSheet: @Composable () -> Unit,
    showBottomSheet: Boolean,
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
                    if (iotData is UIState.FinishedWithSuccessContent<*>) {
                        val hubData = iotData.content as IoTDashboardModel
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
                                    text = hubData.avatarName,
                                    color = MaterialTheme.colorScheme.onSecondary
                                )
                            }
                        }
                    } else if (iotData is UIState.Loading) {
                        CircularProgressIndicator()
                    }
                }
            )
        }
    ) { padding ->
        if (iotData is UIState.Loading) {
            LinearProgressIndicator()
        }

        if (iotData is UIState.FinishedWithSuccessContent<*>) {
            val dashboardModel = iotData.content as IoTDashboardModel
            DashboardPane(
                padding = padding,
                ioTDashboardModel = dashboardModel,
                screenScope = screenScope
            )
        }

        if (showBottomSheet) {
            menuBottomSheet()
        }

    }
}

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun DashboardPane(
    padding: PaddingValues,
    screenScope: CoroutineScope,
    ioTDashboardModel: IoTDashboardModel
) {
    val navigator = rememberListDetailPaneScaffoldNavigator<Device>()

    BackHandler(navigator.canNavigateBack()) {
        navigator.navigateBack()
    }

    val pagerState = rememberPagerState(
        initialPage = 0,
        pageCount = { ioTDashboardModel.rooms.size }
    )
    val selectedRoom = remember(ioTDashboardModel.rooms) {
        ioTDashboardModel.rooms[pagerState.currentPage].id
    }
    Column(modifier = Modifier.padding(padding)) {
        ScrollableTabRow(
            selectedTabIndex = pagerState.currentPage,
            divider = {},
            modifier = Modifier
                .fillMaxWidth()
        ) {
            ioTDashboardModel.rooms.mapIndexed { idx, room ->
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
            ListDetailPaneScaffold(
                directive = navigator.scaffoldDirective,
                value = navigator.scaffoldValue,
                listPane = {
                    AnimatedPane {
                        DevicesList(
                            devices = ioTDashboardModel.devices,
                            roomId = selectedRoom,
                            onDeviceClick = { device ->
                                navigator.navigateTo(ListDetailPaneScaffoldRole.Detail, device)
                            },
                        )
                    }
                },
                detailPane = {
                    AnimatedPane {
                        navigator.currentDestination?.content?.let {
                            MyDetails(it)
                        }
                    }
                }
            )
        }
    }
}

@Composable
fun DevicesList(
    devices: List<Device>,
    roomId: UUID?,
    onDeviceClick: (Device) -> Unit,
) {
    Card {
        LazyColumn {
            devices
                .filter { room -> roomId == null || room.id == roomId }
                .forEachIndexed { id, device ->
                    item {
                        when (device.type) {
                            DeviceType.UPS -> UPSCard(
                                device,
                                device.telemetry
                            ) {
                                onDeviceClick(device)
                            }

                            else -> {}
                        }
                    }
                }
        }
    }
}

@Composable
fun MyDetails(device: Device) {
    val text = device.name
    Card {
        Column(
            Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                text = "Details page device $text",
                fontSize = 24.sp,
            )
            Spacer(Modifier.size(16.dp))
            Text(
                text = "TODO: Add great details here"
            )
        }
    }
}
