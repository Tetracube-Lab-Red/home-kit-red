package red.tetracube.homekitred.ui.iot.device.room

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavHostController
import red.tetracube.homekitred.R
import red.tetracube.homekitred.models.RoomSelectItem
import red.tetracube.homekitred.models.errors.HomeKitRedError
import red.tetracube.homekitred.ui.state.UIState
import red.tetracube.homekitred.ui.state.form.rememberSelectField
import java.util.UUID

@Composable
fun DeviceRoomDialog(
    deviceRoomViewModel: DeviceRoomViewModel,
    navController: NavHostController
) {
    val deviceName = deviceRoomViewModel.loadDevice().collectAsState("").value
    val dialogUIState = deviceRoomViewModel.uiState.collectAsState().value
    val rooms = deviceRoomViewModel.loadRooms().collectAsState(emptyList()).value

    LaunchedEffect(dialogUIState) {
        if (deviceRoomViewModel.uiState.value is UIState.FinishedWithSuccessContent<*>) {
            navController.popBackStack()
        }
    }

    val supportMessage = remember(dialogUIState) {
        if (dialogUIState is UIState.FinishedWithError<*>) {
            if (dialogUIState.error is HomeKitRedError.Unauthorized) {
                "No authorized to update the device's room"
            } else {
                "Some problem with device room update"
            }
        } else {
            ""
        }
    }

    DeviceRoomDialogUI(
        uiState = dialogUIState,
        rooms = rooms,
        deviceName = deviceName,
        supportMessage = supportMessage,
        onDismissRequest = navController::popBackStack,
        onSubmit = deviceRoomViewModel::submitDeviceRoomJoin
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeviceRoomDialogUI(
    uiState: UIState,
    rooms: List<RoomSelectItem>,
    supportMessage: String,
    deviceName: String,
    onDismissRequest: () -> Unit,
    onSubmit: (UUID) -> Unit
) {
    val roomSelectField = rememberSelectField<RoomSelectItem>(null)
    rooms.firstOrNull { r -> r.selected }
        ?.apply {
            roomSelectField.setValue(this.roomName)
            roomSelectField.setOptionValue(this)
        }

    BasicAlertDialog(
        onDismissRequest = onDismissRequest,
        modifier = Modifier,
        properties = DialogProperties()
    ) {
        Card(
            modifier = Modifier,
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(imageVector = Icons.Rounded.Add, contentDescription = null)
                    Spacer(modifier = Modifier.size(8.dp))
                    Text(
                        text = "Join $deviceName in room",
                        style = MaterialTheme.typography.headlineSmall
                    )
                }

                if (uiState !is UIState.FinishedWithError<*>) {
                    ExposedDropdownMenuBox(
                        expanded = roomSelectField.expanded,
                        onExpandedChange = { roomSelectField.toggleSelect() },
                    ) {
                        OutlinedTextField(
                            leadingIcon = {
                                Icon(
                                    painter = painterResource(R.drawable.room_preferences_24px),
                                    contentDescription = null
                                )
                            },
                            modifier = Modifier
                                .menuAnchor(MenuAnchorType.PrimaryNotEditable)
                                .fillMaxWidth(),
                            value = roomSelectField.value,
                            supportingText = { Text(supportMessage) },
                            onValueChange = { },
                            readOnly = true,
                            singleLine = true,
                            label = { Text("Room to join") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = roomSelectField.expanded) },
                        )
                        ExposedDropdownMenu(
                            expanded = roomSelectField.expanded,
                            onDismissRequest = { roomSelectField.toggleSelect() },
                        ) {
                            rooms.map { option ->
                                DropdownMenuItem(
                                    text = {
                                        Text(
                                            option.roomName,
                                            style = MaterialTheme.typography.bodyLarge
                                        )
                                    },
                                    onClick = {
                                        roomSelectField.setValue(option.roomName)
                                        roomSelectField.setOptionValue(option)
                                        roomSelectField.toggleSelect()
                                    },
                                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                                )
                            }
                        }
                    }
                } else {
                    Text("Device not found")
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(
                        enabled = uiState is UIState.Neutral || uiState is UIState.FinishedWithError<*>,
                        onClick = onDismissRequest
                    ) {
                        Text("Cancel")
                    }

                    if (uiState is UIState.Loading) {
                        CircularProgressIndicator(modifier = Modifier.size(ButtonDefaults.IconSize))
                    } else if (uiState !is UIState.FinishedWithError<*> || uiState.error !is HomeKitRedError.NotFound) {
                        TextButton(
                            enabled = true,
                            onClick = {
                                roomSelectField.option?.roomId?.apply {
                                    onSubmit(this)
                                }
                            }
                        ) {
                            Text("Join")
                        }
                    }
                }
            }
        }
    }
}