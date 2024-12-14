package red.tetracube.homekitred.ui.hub.setup

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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavHostController
import kotlinx.coroutines.launch
import red.tetracube.homekitred.R
import red.tetracube.homekitred.business.models.errors.HomeKitRedError
import red.tetracube.homekitred.business.models.ui.UIState
import red.tetracube.homekitred.ui.form.rememberFormState
import red.tetracube.homekitred.ui.form.rememberPasswordField
import red.tetracube.homekitred.ui.form.rememberTextField
import red.tetracube.homekitred.ui.hub.login.validateHostAddress
import red.tetracube.homekitred.ui.hub.login.validateHubName
import red.tetracube.homekitred.ui.hub.login.validatePassword

@Composable
fun HubSetupScreen(
    navHostController: NavHostController,
    hubSetupViewModel: HubSetupViewModel
) {
    val uiState = hubSetupViewModel.uiState.collectAsState().value
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val shouldShowDialog = remember { mutableStateOf(false) }

    LaunchedEffect(uiState) {
        if (uiState is UIState.FinishedWithError<*>) {
            val message = when (uiState.error) {
                HomeKitRedError.Conflict -> "There is another Hub configured"
                else -> "Hub error"
            }
            scope.launch {
                snackbarHostState.showSnackbar(
                    message = message,
                    withDismissAction = true,
                    duration = SnackbarDuration.Long
                )
            }
        } else if (uiState is UIState.FinishedWithSuccessContent<*>) {
            shouldShowDialog.value = true
        }
    }

    HubSetupScreenUI(
        uiState = uiState,
        onBackButtonClick = {
            navHostController.popBackStack()
        },
        onSetupButtonClick = { hubAddress, hubName, hubPassword ->
            hubSetupViewModel.onSetupButtonClick(hubAddress, hubName, hubPassword)
        },
        snackbarHostState = snackbarHostState,
        shouldShowDialog = shouldShowDialog.value,
        onDialogConfirm = {
            navHostController.popBackStack()
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HubSetupScreenUI(
    uiState: UIState,
    onBackButtonClick: () -> Unit,
    onSetupButtonClick: (String, String, String) -> Unit,
    snackbarHostState: SnackbarHostState,
    shouldShowDialog: Boolean,
    onDialogConfirm: () -> Unit
) {
    val focusRequester = LocalFocusManager.current
    val hubAddressField = rememberTextField { validateHostAddress(it) }
    val hubNameField = rememberTextField { validateHubName(it) }
    val hubPasswordField = rememberPasswordField { validatePassword(it) }
    val formState = rememberFormState(listOf(hubAddressField, hubNameField, hubPasswordField))
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
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(horizontal = 16.dp),
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
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Hub host address") },
                        value = hubAddressField.value,
                        onValueChange = { value: String -> hubAddressField.setValue(value) },
                        singleLine = true,
                        maxLines = 1,
                        supportingText = {
                            hubAddressField.message?.let {
                                Text(it)
                            }
                        },
                        isError = hubAddressField.hasError(),
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
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Hub name") },
                        value = hubNameField.value,
                        onValueChange = { value: String -> hubNameField.setValue(value) },
                        singleLine = true,
                        maxLines = 1,
                        supportingText = {
                            hubAddressField.message?.let {
                                Text(it)
                            }
                        },
                        isError = hubAddressField.hasError(),
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
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Password") },
                        value = hubPasswordField.value,
                        onValueChange = { value: String -> hubPasswordField.setValue(value) },
                        singleLine = true,
                        maxLines = 1,
                        supportingText = {
                            hubPasswordField.message?.let {
                                Text(it)
                            }
                        },
                        isError = hubPasswordField.hasError(),
                        visualTransformation =
                        if (!hubPasswordField.showPassword) {
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
                                onClick = { hubPasswordField.togglePasswordVisibility() }
                            ) {
                                val icon = if (hubPasswordField.showPassword) {
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
                    enabled = formState.isValid && uiState !is UIState.Loading,
                    onClick = {
                        focusRequester.clearFocus()
                        onSetupButtonClick(
                            hubAddressField.value,
                            hubNameField.value,
                            hubPasswordField.value
                        )
                    }
                ) {
                    Text("Setup")
                }
            }
        }

        if (shouldShowDialog) {
            BasicAlertDialog(
                onDismissRequest = { },
                properties = DialogProperties(
                    dismissOnClickOutside = false,
                    dismissOnBackPress = false
                )
            ) {
                Surface(
                    modifier = Modifier
                        .wrapContentWidth()
                        .wrapContentHeight(),
                    shape = MaterialTheme.shapes.large,
                    tonalElevation = AlertDialogDefaults.TonalElevation
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.check_circle_24px),
                                contentDescription = null
                            )
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text(
                                "Hub created",
                                style = MaterialTheme.typography.titleLarge
                            )
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                        ) {
                            Text(
                                "The hub is created correctly, now you will be redirected to the Sign in page to proceed with login in the brand new hub"
                            )
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End
                        ) {
                            TextButton(
                                onClick = onDialogConfirm
                            ) {
                                Text("Return to Sign in")
                            }
                        }
                    }
                }
            }
        }
    }
}