package red.tetracube.homekitred.ui.setup

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import red.tetracube.homekitred.R
import red.tetracube.homekitred.app.behaviour.routing.Routes
import red.tetracube.homekitred.ui.core.models.UIState
import red.tetracube.homekitred.ui.login.models.FieldInputEvent
import red.tetracube.homekitred.ui.login.models.FieldInputEvent.FieldName
import red.tetracube.homekitred.ui.setup.models.HubSetupUIModel

@Composable
fun HubSetupScreen(
    navHostController: NavHostController,
    hubSetupViewModel: HubSetupViewModel
) {
    val formStatus = hubSetupViewModel.hubSetupModel.value
    val uiState = hubSetupViewModel.uiState.value

    if (uiState is UIState.FinishedWithError<*>) {
        navHostController.navigate(
            Routes.ErrorDialog(uiState.error.toString())
        )
    }

    HubSetupScreenUI(
        uiState = uiState,
        formStatus = formStatus,
        onInputFocus = { fieldName: FieldName ->
            hubSetupViewModel.onInputEvent(FieldInputEvent.FieldFocusAcquire(fieldName))
        },
        onTextInput = { fieldName: FieldName, value: String ->
            hubSetupViewModel.onInputEvent(FieldInputEvent.FieldValueInput(fieldName, value))
        },
        onFieldTrailingIconClick = {
            hubSetupViewModel.onInputEvent(FieldInputEvent.FieldTrailingButtonClick(FieldName.PASSWORD))
        },
        onBackButtonClick = {
            navHostController.popBackStack()
        },
        onSetupButtonClick = {
            hubSetupViewModel.onSetupButtonClick()
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HubSetupScreenUI(
    uiState: UIState,
    formStatus: HubSetupUIModel,
    onInputFocus: (FieldName) -> Unit,
    onTextInput: (FieldName, String) -> Unit,
    onFieldTrailingIconClick: () -> Unit,
    onBackButtonClick: () -> Unit,
    onSetupButtonClick: () -> Unit
) {
    val focusRequester = LocalFocusManager.current
    Scaffold(
        modifier = Modifier
            .clickable(
                indication = null,
                interactionSource = remember {
                    MutableInteractionSource()
                }
            ) { focusRequester.clearFocus() },
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Setup new hub"
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            onBackButtonClick()
                        }
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.arrow_back_ios_24px),
                            contentDescription = null
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            if (uiState is UIState.Loading) {
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.fillMaxWidth(0.1f)) {
                    Icon(painter = painterResource(R.drawable.http_24px), null)
                }
                Column {
                    OutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .onFocusChanged {
                                if (!it.isFocused) onInputFocus(FieldName.HUB_ADDRESS)
                            },
                        label = { Text("Hub host address") },
                        value = formStatus.hubAddressField.value,
                        onValueChange = { value: String ->
                            onTextInput(
                                FieldName.HUB_ADDRESS,
                                value
                            )
                        },
                        singleLine = true,
                        maxLines = 1,
                        supportingText = {
                            val supportingTextValue = if (formStatus.hubAddressField.hasError) {
                                "Invalid HUB Address"
                            } else {
                                "Required"
                            }
                            Text(supportingTextValue)
                        },
                        isError = formStatus.hubAddressField.hasError,
                        keyboardOptions = KeyboardOptions.Default.copy(
                            autoCorrectEnabled = false,
                            keyboardType = KeyboardType.Uri
                        )
                    )
                }
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.fillMaxWidth(0.1f)) {
                    Icon(painter = painterResource(R.drawable.hub_24px), null)
                }
                Column {
                    OutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .onFocusChanged {
                                if (it.isFocused) onInputFocus(FieldName.HUB_NAME)
                            },
                        label = { Text("Hub name") },
                        value = formStatus.hubNameField.value,
                        onValueChange = { value: String -> onTextInput(FieldName.HUB_NAME, value) },
                        singleLine = true,
                        maxLines = 1,
                        supportingText = {
                            val supportingTextValue = if (formStatus.hubNameField.hasError) {
                                "Invalid HUB Name"
                            } else {
                                "Required"
                            }
                            Text(supportingTextValue)
                        },
                        isError = formStatus.hubNameField.hasError,
                        keyboardOptions = KeyboardOptions.Default.copy(
                            autoCorrectEnabled = false,
                            keyboardType = KeyboardType.Text
                        )
                    )
                }
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.fillMaxWidth(0.1f)) {
                    Icon(painter = painterResource(R.drawable.password_24px), null)
                }
                Column {
                    OutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .onFocusChanged {
                                if (it.isFocused) onInputFocus(FieldName.PASSWORD)
                            },
                        label = { Text("Password") },
                        value = formStatus.hubPasswordField.value,
                        onValueChange = { value: String -> onTextInput(FieldName.PASSWORD, value) },
                        singleLine = true,
                        maxLines = 1,
                        supportingText = {
                            val supportingTextValue = if (formStatus.hubPasswordField.hasError) {
                                "Invalid password"
                            } else {
                                "Required"
                            }
                            Text(supportingTextValue)
                        },
                        isError = formStatus.hubPasswordField.hasError,
                        visualTransformation =
                        if (formStatus.hubPasswordField.clearPassword) {
                            VisualTransformation.None
                        } else {
                            PasswordVisualTransformation()
                        },
                        keyboardOptions = KeyboardOptions.Default.copy(
                            autoCorrectEnabled = false,
                            keyboardType = KeyboardType.Password
                        ),
                        trailingIcon = {
                            IconButton(
                                onClick = {
                                    onFieldTrailingIconClick()
                                }
                            ) {
                                val icon = if (formStatus.hubPasswordField.clearPassword) {
                                    R.drawable.visibility_off_24px
                                } else {
                                    R.drawable.visibility_24px
                                }
                                Icon(painter = painterResource(icon), contentDescription = null)
                            }
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                FilledTonalButton(
                    colors = ButtonDefaults.filledTonalButtonColors(),
                    modifier = Modifier.fillMaxWidth(),
                    enabled = formStatus.formIsValid && uiState !is UIState.Loading,
                    onClick = { onSetupButtonClick() }
                ) {
                    Text("Setup")
                }
            }
        }
    }
}