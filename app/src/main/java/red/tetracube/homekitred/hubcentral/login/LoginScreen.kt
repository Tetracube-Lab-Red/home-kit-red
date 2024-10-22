package red.tetracube.homekitred.hubcentral.login

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
import androidx.compose.material3.CenterAlignedTopAppBar
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
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import kotlinx.coroutines.launch
import red.tetracube.homekitred.R
import red.tetracube.homekitred.app.behaviour.routing.Routes
import red.tetracube.homekitred.domain.HomeKitRedError
import red.tetracube.homekitred.hubcentral.login.models.FieldInputEvent
import red.tetracube.homekitred.hubcentral.login.models.LoginUIModel
import red.tetracube.homekitred.ui.core.models.UIState

@Composable
fun LoginScreen(
    navHostController: NavHostController,
    loginViewModel: LoginViewModel
) {
    val formStatus = loginViewModel.loginUIModel.value
    val uiState = loginViewModel.uiState.value
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(uiState) {
        if (uiState is UIState.FinishedWithError<*>) {
            val message = when (uiState.error) {
                HomeKitRedError.ClientError -> "There was an error in the hub login"
                HomeKitRedError.GenericError -> "There was an error in the hub login"
                HomeKitRedError.ServiceError -> "Cannot login in the hub for an hub platform error"
                HomeKitRedError.Unauthorized -> "You are not authorized to login in this hub"
                HomeKitRedError.UnprocessableResult -> "The hub login returned in unexpected response"
                HomeKitRedError.UnreachableService -> "The hub platform is unreachable"
                else -> "Unhandled error"
            }
            scope.launch {
                snackbarHostState.showSnackbar(
                    message = message,
                    withDismissAction = true,
                    duration = SnackbarDuration.Long
                )
            }
        } else if (uiState is UIState.FinishedWithSuccess) {
            navHostController.navigate(Routes.IoT) {
                popUpTo<Routes.Login>() { inclusive = true }
            }
        }
    }

    LoginScreenUI(
        formStatus = formStatus,
        snackbarHostState = snackbarHostState,
        uiState = uiState,
        onInputFocus = { fieldName: FieldInputEvent.FieldName ->
            loginViewModel.onInputEvent(FieldInputEvent.FieldFocusAcquire(fieldName))
        },
        onTextInput = { fieldName: FieldInputEvent.FieldName, value: String ->
            loginViewModel.onInputEvent(FieldInputEvent.FieldValueInput(fieldName, value))
        },
        onFieldTrailingIconClick = {
            loginViewModel.onInputEvent(FieldInputEvent.FieldTrailingButtonClick(FieldInputEvent.FieldName.PASSWORD))
        },
        onSetupHuButtonClick = {
            navHostController.navigate(Routes.HubSetup) {
                launchSingleTop = true
            }
        },
        onDialogConfirm = {
            loginViewModel.onLoginButtonClick()
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreenUI(
    formStatus: LoginUIModel,
    onInputFocus: (FieldInputEvent.FieldName) -> Unit,
    onTextInput: (FieldInputEvent.FieldName, String) -> Unit,
    onFieldTrailingIconClick: () -> Unit,
    onSetupHuButtonClick: () -> Unit,
    snackbarHostState: SnackbarHostState,
    onDialogConfirm: () -> Unit,
    uiState: UIState
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
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        buildAnnotatedString {
                            withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.primary)) {
                                append("HomeKit")
                            }
                            withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.tertiary)) {
                                append(" RED")
                            }
                        },
                        style = MaterialTheme.typography.headlineLarge,
                        color = MaterialTheme.colorScheme.tertiary
                    )
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
                .padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            if (uiState is UIState.Loading) {
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    "Sign In",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.tertiary,
                )
            }
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
                                if (!it.isFocused) onInputFocus(FieldInputEvent.FieldName.HUB_ADDRESS)
                            },
                        label = { Text("Hub host address") },
                        value = formStatus.hubAddressField.value,
                        onValueChange = { value: String ->
                            onTextInput(
                                FieldInputEvent.FieldName.HUB_ADDRESS,
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
                                if (it.isFocused) onInputFocus(FieldInputEvent.FieldName.HUB_NAME)
                            },
                        label = { Text("Hub name") },
                        value = formStatus.hubNameField.value,
                        onValueChange = { value: String -> onTextInput(FieldInputEvent.FieldName.HUB_NAME, value) },
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
                                if (it.isFocused) onInputFocus(FieldInputEvent.FieldName.PASSWORD)
                            },
                        label = { Text("Password") },
                        value = formStatus.hubPasswordField.value,
                        onValueChange = { value: String -> onTextInput(FieldInputEvent.FieldName.PASSWORD, value) },
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
            ) {
                FilledTonalButton(
                    colors = ButtonDefaults.filledTonalButtonColors(),
                    modifier = Modifier.fillMaxWidth(),
                    enabled = formStatus.formIsValid && uiState !is UIState.Loading,
                    onClick = {
                        focusRequester.clearFocus()
                        onDialogConfirm()
                    }
                ) {
                    Text("Sign in")
                }
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Absolute.Right
            ) {
                TextButton(
                    modifier = Modifier,
                    onClick = {
                        onSetupHuButtonClick()
                    }
                ) {
                    Text("Setup a new Hub")
                }
            }
        }
    }
}
