package red.tetracube.homekitred.ui.iot.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import red.tetracube.homekitred.domain.HubWithRooms

@Composable
fun IoTHomeScreen(
    viewModel: IoTHomeViewModel
) {
    val hub = viewModel.hub
    val pagerState = rememberPagerState(initialPage = 0, pageCount = { 3 })
    val screenScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        viewModel.loadHubData()
    }

    IoTHomeScreenUI(
        hub = hub.value,
        pagerState = pagerState,
        onTabClick = { idx ->
            screenScope.launch {
                pagerState.animateScrollToPage(idx)
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IoTHomeScreenUI(
    hub: HubWithRooms?,
    pagerState: PagerState,
    onTabClick: (Int) -> Unit
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
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(color = MaterialTheme.colorScheme.secondary),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = hub?.avatarName ?: "N.A.",
                            color = MaterialTheme.colorScheme.onSecondary
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(innerPadding)
        ) {
            ScrollableTabRow(
                selectedTabIndex = pagerState.currentPage,
                divider = {
                    Spacer(modifier = Modifier.height(5.dp))
                },
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Tab(
                    selected = pagerState.currentPage == 0,
                    onClick = {
                        onTabClick(0)
                    },
                    text = {
                        Text(text = "Unqualified")
                    }
                )

                Tab(
                    selected = pagerState.currentPage == 1,
                    onClick = {
                        onTabClick(1)
                    },
                    text = {
                        Text(text = "Living room")
                    }
                )

                Tab(
                    selected = pagerState.currentPage == 2,
                    onClick = {
                        onTabClick(2)
                    },
                    text = {
                        Text(text = "Studio")
                    }
                )
            }
            HorizontalPager(state = pagerState) { index ->
                when (index) {
                    0 -> {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text("0")
                        }
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
        }
    }
}