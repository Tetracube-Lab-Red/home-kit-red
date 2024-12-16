package red.tetracube.homekitred.ui.iot.home

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.ListItem
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
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import red.tetracube.homekitred.business.enumerations.DeviceType
import red.tetracube.homekitred.iot.home.components.MenuBottomSheet
import red.tetracube.homekitred.iot.home.components.UPSCard
import red.tetracube.homekitred.iot.home.domain.models.BasicTelemetry
import red.tetracube.homekitred.iot.home.domain.models.BottomSheetItem
import red.tetracube.homekitred.iot.home.domain.models.HubWithRooms
import red.tetracube.homekitred.iot.home.domain.models.deviceMenuItems
import red.tetracube.homekitred.iot.home.domain.models.globalMenuItems
import red.tetracube.homekitred.ui.iot.home.models.Device
import red.tetracube.homekitred.ui.iot.home.models.IoTDashboardModel
import red.tetracube.homekitred.ui.iot.home.models.MyItem
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
    val uiState = viewModel.uiState.value
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
    val deviceMenuItemsBuilder: (UUID) -> Unit = { deviceId ->
        menuItems.clear()
        menuItems.addAll(deviceMenuItems(navController, deviceId, toggleBottomSheet))
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

    IotHomeScreenUINew(
        iotData = iotDataFlow.value,
        onHubAvatarClick = globalMenuItemsBuilder,
        screenScope = screenScope,
        showBottomSheet = showBottomSheet.value,
        menuBottomSheet = {
            MenuBottomSheet(
                sheetState = sheetState,
                onModalDismissRequest = toggleBottomSheet,
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
    val navigator = rememberListDetailPaneScaffoldNavigator<MyItem>()

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
                        MyList(
                            devices = ioTDashboardModel.list,
                            onItemClick = { item ->
                                navigator.navigateTo(ListDetailPaneScaffoldRole.Detail, item)
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
fun MyList(
    devices: List<String>,
    onItemClick: (MyItem) -> Unit,
) {
    Card {
        LazyColumn {
            devices.forEachIndexed { id, string ->
                item {
                    ListItem(
                        modifier = Modifier
                            .background(Color.Magenta)
                            .clickable {
                                onItemClick(MyItem(id))
                            },
                        headlineContent = {
                            Text(
                                text = string,
                            )
                        },
                    )
                }
            }
        }
    }
}

@Composable
fun MyDetails(item: MyItem) {
    val text = shortStrings[item.id]
    Card {
        Column(
            Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                text = "Details page for $text",
                fontSize = 24.sp,
            )
            Spacer(Modifier.size(16.dp))
            Text(
                text = "TODO: Add great details here"
            )
        }
    }
}

val shortStrings = listOf(
    "Cupcake",
    "Donut",
    "Eclair",
    "Froyo",
    "Gingerbread",
    "Honeycomb",
    "Ice cream sandwich",
    "Jelly bean",
    "Kitkat",
    "Lollipop",
    "Marshmallow",
    "Nougat",
    "Oreo",
    "Pie",
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IoTHomeScreenUI(
    hub: HubWithRooms?,
    screenScope: CoroutineScope,
    menuBottomSheet: @Composable () -> Unit,
    showBottomSheet: Boolean,
    devicesTelemetriesMap: Map<Device, BasicTelemetry>,
    onDeviceMenuRequest: (UUID) -> Unit
) {
    Scaffold(

    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {


            val pagerState =
                rememberPagerState(initialPage = 0, pageCount = { hub?.rooms?.size ?: 0 })
            val selectedRoomSlug = remember(hub?.rooms) {
                derivedStateOf {
                    hub?.rooms[pagerState.currentPage]?.id
                }
            }
            ScrollableTabRow(
                selectedTabIndex = pagerState.currentPage,
                divider = {
                },
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                hub?.rooms?.mapIndexed { idx, room ->
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

        if (showBottomSheet) {
            menuBottomSheet()
        }
    }
}

@Composable
fun DevicesGrid(
    roomId: UUID?,
    devicesTelemetriesMap: Map<Device, BasicTelemetry>,
    onDeviceMenuRequest: (UUID) -> Unit
) {
    LazyColumn {
        devicesTelemetriesMap
            .filter { room ->
                roomId == null || room.key.id == roomId
            }
            .map { deviceTelemetryEntry ->
                item {
                    when (deviceTelemetryEntry.key.type) {
                        DeviceType.UPS -> UPSCard(
                            deviceTelemetryEntry.key,
                            deviceTelemetryEntry.value,
                            onDeviceMenuRequest
                        )

                        else -> {}
                    }
                }
            }
    }
}

