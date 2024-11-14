package red.tetracube.homekitred.hubcentral.room.create

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavHostController
import red.tetracube.homekitred.app.exceptions.HomeKitRedError
import red.tetracube.homekitred.hubcentral.room.create.models.FieldInputEvent
import red.tetracube.homekitred.hubcentral.room.create.models.FieldInputEvent.FieldName
import red.tetracube.homekitred.hubcentral.room.create.models.RoomCreateUIModel
import red.tetracube.homekitred.app.models.UIState


@Composable
fun RoomCreateDialog(
    navController: NavHostController,
    viewModel: RoomCreateViewModel
) {
    val formState = viewModel.roomCreateState
    val uiState = viewModel.uiState

    LaunchedEffect(uiState.value) {
        if (uiState.value is UIState.FinishedWithSuccess) {
            navController.popBackStack()
        }
    }

    RoomCreateDialogUI(
        formState = formState.value,
        uiState = uiState.value,
        onInputFocus = { fieldName: FieldName ->
            viewModel.onInputEvent(FieldInputEvent.FieldFocusAcquire(fieldName))
        },
        onTextInput = { fieldName: FieldName, value: String ->
            viewModel.onInputEvent(FieldInputEvent.FieldValueInput(fieldName, value))
        },
        onDismissRequest = {
            navController.popBackStack()
        },
        onSubmit = {
            viewModel.onRoomSubmit()
        },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoomCreateDialogUI(
    formState: RoomCreateUIModel,
    uiState: UIState,
    onInputFocus: (FieldName) -> Unit,
    onTextInput: (FieldName, String) -> Unit,
    onDismissRequest: () -> Unit,
    onSubmit: () -> Unit
) {
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
                    modifier = Modifier
                        .fillMaxWidth()
                        .onFocusChanged {
                            if (it.isFocused) onInputFocus(FieldName.ROOM_NAME)
                        },
                    label = { Text("Hub name") },
                    value = formState.roomNameField.value,
                    onValueChange = { value: String -> onTextInput(FieldName.ROOM_NAME, value) },
                    singleLine = true,
                    maxLines = 1,
                    supportingText = {
                        Text(formState.roomNameField.supportingText)
                    },
                    isError = formState.roomNameField.isDirty && !formState.roomNameField.isValid,
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
                            enabled = formState.formIsValid,
                            onClick = { onSubmit() }
                        ) {
                            Text("OK")
                        }
                    }
                }
            }
        }
    }
}