package red.tetracube.homekitred.iot.home.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import red.tetracube.homekitred.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuBottomSheet(
    sheetState: SheetState,
    onModalDismissRequest: () -> Unit,
    onAddRoomClick: () -> Unit,
    onAddDeviceClick: () -> Unit
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
                    colors = ButtonDefaults.textButtonColors().copy(contentColor = MaterialTheme.colorScheme.tertiary),
                    onClick = {
                        onAddRoomClick()
                    }
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            painter = painterResource(R.drawable.room_preferences_24px),
                            contentDescription = null
                        )
                        Spacer(modifier = Modifier.size(ButtonDefaults.IconSpacing))
                        Text("New room")
                    }
                }
            }

            item(span = {
                GridItemSpan(1)
            }) {
                TextButton(
                    colors = ButtonDefaults.textButtonColors().copy(contentColor = MaterialTheme.colorScheme.tertiary),
                    onClick = {
                        onAddDeviceClick()
                    }
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            painter = painterResource(R.drawable.home_iot_device_24px),
                            contentDescription = null
                        )
                        Spacer(modifier = Modifier.size(ButtonDefaults.IconSpacing))
                        Text("Add device")
                    }
                }
            }
        }
    }
}