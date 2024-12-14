package red.tetracube.homekitred.ui.hub.room.create

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavHostController
import red.tetracube.homekitred.business.models.errors.HomeKitRedError
import red.tetracube.homekitred.business.models.ui.UIState
import red.tetracube.homekitred.ui.form.rememberFormState
import red.tetracube.homekitred.ui.form.rememberTextField


@Composable
fun RoomCreateDialog(
    navController: NavHostController,
    viewModel: RoomCreateViewModel
) {
    val uiState = viewModel.uiState.collectAsState().value
    LaunchedEffect(uiState) {
        if (uiState is UIState.FinishedWithSuccessContent<*>) {
            navController.popBackStack()
        }
    }

    RoomCreateDialogUI(
        uiState = uiState,
        onDismissRequest = {
            navController.popBackStack()
        },
        onSubmit = { roomName ->
            viewModel.onRoomSubmit(roomName)
        },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoomCreateDialogUI(
    uiState: UIState,
    onDismissRequest: () -> Unit,
    onSubmit: (String) -> Unit
) {
    val roomNameField = rememberTextField { validateRoomName(it) }
    val formState = rememberFormState(listOf(roomNameField))

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
                        text = "New room",
                        style = MaterialTheme.typography.headlineSmall
                    )
                }

                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Room name") },
                    value = roomNameField.value,
                    onValueChange = { value: String -> roomNameField.setValue(value) },
                    singleLine = true,
                    maxLines = 1,
                    supportingText = {
                        roomNameField.message?.let {
                            Text(it)
                        }
                    },
                    isError = roomNameField.hasError(),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        autoCorrectEnabled = true,
                        keyboardType = KeyboardType.Text
                    )
                )

                if (uiState is UIState.FinishedWithError<*>) {
                    val message = when (uiState.error) {
                        HomeKitRedError.Conflict -> "This room already exists"
                        HomeKitRedError.ServiceError -> "Cannot create the room for an hub platform error"
                        HomeKitRedError.Unauthorized -> "You are not authorized to create a room in this hub"
                        HomeKitRedError.UnprocessableResult -> "The room creation returned in unexpected response"
                        HomeKitRedError.UnreachableService -> "The hub platform is unreachable"
                        else -> "There was an error in the room creation"
                    }
                    Text(
                        text = message,
                        color = MaterialTheme.colorScheme.error
                    )
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
                    } else {
                        TextButton(
                            enabled = formState.isValid,
                            onClick = { onSubmit(roomNameField.value) }
                        ) {
                            Text("OK")
                        }
                    }
                }
            }
        }
    }
}