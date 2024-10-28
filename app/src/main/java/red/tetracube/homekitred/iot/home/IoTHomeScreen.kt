package red.tetracube.homekitred.iot.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridItemSpan
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.SheetState
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import androidx.navigation.NavHostController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import red.tetracube.homekitred.R
import red.tetracube.homekitred.app.behaviour.routing.Routes
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
                        HorizontalDivider()
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
                    when (index) {
                        0 -> {
                            DevicesGrid()
                        }

                        1 -> {
                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text("1")
                            }
                        }

                        2 -> {
                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text("2")
                            }
                        }

                        3 -> {
                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text("3")
                            }
                        }
                    }
                }

                if (showBottomSheet) {
                    menuBottomSheet()
                }
            }
        }
    }
}

@Composable
fun DevicesGrid() {
    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Fixed(2)
    ) {
        item(span = StaggeredGridItemSpan.FullLine) {
            ElevatedCard(
                modifier = Modifier.padding(8.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Icon(
                            modifier = Modifier
                                .clip(shape = CircleShape)
                                .background(color = MaterialTheme.colorScheme.secondaryContainer),
                            tint = MaterialTheme.colorScheme.onSecondaryContainer,
                            painter = painterResource(R.drawable.power_48px),
                            contentDescription = null,
                        )

                        Column(modifier = Modifier) {
                            Text(
                                "UPS Studio",
                                style = MaterialTheme.typography.titleSmall
                            )
                            Text(
                                "Online Battery Charge",
                                style = MaterialTheme.typography.titleSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                    Column(modifier = Modifier) {
                        LazyRow(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            item {
                                Text(
                                    buildAnnotatedString {
                                        withStyle(
                                            style = MaterialTheme.typography.headlineSmall
                                                .copy(color = MaterialTheme.colorScheme.tertiary)
                                                .toSpanStyle()
                                        ) {
                                            append("231v")
                                        }
                                        withStyle(style = MaterialTheme.typography.bodySmall.toSpanStyle()) {
                                            append("\nInput Voltage")
                                        }
                                    },
                                    textAlign = TextAlign.Center,
                                )
                            }
                            item {
                                Text(
                                    buildAnnotatedString {
                                        withStyle(
                                            style = MaterialTheme.typography.headlineSmall
                                                .copy(color = MaterialTheme.colorScheme.tertiary)
                                                .toSpanStyle()
                                        ) {
                                            append("220v")
                                        }
                                        withStyle(style = MaterialTheme.typography.bodySmall.toSpanStyle()) {
                                            append("\nOutput Voltage")
                                        }
                                    },
                                    textAlign = TextAlign.Center,
                                )
                            }
                            item {
                                Text(
                                    buildAnnotatedString {
                                        withStyle(
                                            style = MaterialTheme.typography.headlineSmall
                                                .copy(color = MaterialTheme.colorScheme.tertiary)
                                                .toSpanStyle()
                                        ) {
                                            append("98%")
                                        }
                                        withStyle(style = MaterialTheme.typography.bodySmall.toSpanStyle()) {
                                            append("\nBattery Charge")
                                        }
                                    },
                                    textAlign = TextAlign.Center,
                                )
                            }
                            item {
                                Text(
                                    buildAnnotatedString {
                                        withStyle(
                                            style = MaterialTheme.typography.headlineSmall
                                                .copy(color = MaterialTheme.colorScheme.tertiary)
                                                .toSpanStyle()
                                        ) {
                                            append("23%")
                                        }
                                        withStyle(style = MaterialTheme.typography.bodySmall.toSpanStyle()) {
                                            append("\nLoad")
                                        }
                                    },
                                    textAlign = TextAlign.Center,
                                )
                            }

                            item {
                                Text(
                                    buildAnnotatedString {
                                        withStyle(
                                            style = MaterialTheme.typography.headlineSmall
                                                .copy(color = MaterialTheme.colorScheme.tertiary)
                                                .toSpanStyle()
                                        ) {
                                            append("1")
                                        }
                                        withStyle(style = MaterialTheme.typography.bodySmall.toSpanStyle()) {
                                            append("\nNotification")
                                        }
                                    },
                                    textAlign = TextAlign.Center,
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuBottomSheet(
    sheetState: SheetState,
    onModalDismissRequest: () -> Unit,
    onAddRoomClick: () -> Unit
) {
    ModalBottomSheet(
        modifier = Modifier.wrapContentHeight(),
        sheetState = sheetState,
        onDismissRequest = { onModalDismissRequest() }
    ) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            contentPadding = PaddingValues(8.dp)
        ) {
            item(span = {
                GridItemSpan(1)
            }) {
                TextButton(
                    onClick = {
                        onAddRoomClick()
                    }
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Add,
                        contentDescription = null,
                        modifier = Modifier.size(ButtonDefaults.IconSize)
                    )
                    Spacer(modifier = Modifier.size(ButtonDefaults.IconSpacing))
                    Text("Add room")
                }
            }
        }
    }
}