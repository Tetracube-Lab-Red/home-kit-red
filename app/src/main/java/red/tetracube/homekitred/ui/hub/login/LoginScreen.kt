package red.tetracube.homekitred.ui.hub.login

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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import red.tetracube.homekitred.models.errors.HomeKitRedError
import red.tetracube.homekitred.ui.state.UIState
import red.tetracube.homekitred.navigation.Routes
import red.tetracube.homekitred.ui.state.form.rememberFormState
import red.tetracube.homekitred.ui.state.form.rememberPasswordField
import red.tetracube.homekitred.ui.state.form.rememberTextField

@Composable
fun LoginScreen(
    navHostController: NavHostController,
    loginViewModel: LoginViewModel
) {
    val uiState = loginViewModel.uiState.collectAsState().value
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(uiState) {
        if (uiState is UIState.FinishedWithError<*>) {
            val message = when (uiState.error) {
                HomeKitRedError.Unauthorized -> "You are not authorized to login in this hub"
                else -> "There was a problem while contacting your hub, check the connectivity and hub status"
            }
            scope.launch {
                snackbarHostState.showSnackbar(
                    message = message,
                    withDismissAction = true,
                    duration = SnackbarDuration.Long
                )
            }
        } else if (uiState is UIState.FinishedWithSuccessContent<*>) {
            navHostController.navigate(Routes.IoT) {
                popUpTo<Routes.Login>() { inclusive = true }
            }
        }
    }

    LoginScreenUI(
        snackbarHostState = snackbarHostState,
        uiState = uiState,
        onSetupHuButtonClick = {
            navHostController.navigate(Routes.HubSetup) {
                launchSingleTop = true
            }
        },
        onFormConfirm = loginViewModel::onLoginButtonClick
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreenUI(
    onSetupHuButtonClick: () -> Unit,
    snackbarHostState: SnackbarHostState,
    onFormConfirm: (String, String, String) -> Unit,
    uiState: UIState
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
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Hub host address") },
                        value = hubAddressField.value,
                        onValueChange = { value: String ->
                            hubAddressField.setValue(value)
                        },
                        singleLine = true,
                        maxLines = 1,
                        supportingText = {
                            hubAddressField.getSupportingMessage()?.let {
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
                        onValueChange = { value: String ->
                            hubNameField.setValue(value)
                        },
                        singleLine = true,
                        maxLines = 1,
                        supportingText = {
                            hubNameField.getSupportingMessage()?.let {
                                Text(it)
                            }
                        },
                        isError = hubNameField.hasError(),
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
                        onValueChange = { value: String ->
                            hubPasswordField.setValue(value)
                        },
                        singleLine = true,
                        maxLines = 1,
                        supportingText = {
                            hubPasswordField.getSupportingMessage()?.let {
                                Text(it)
                            }
                        },
                        isError = hubPasswordField.hasError(),
                        visualTransformation =
                        if (hubPasswordField.showPassword) {
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
                                    hubPasswordField.togglePasswordVisibility()
                                }
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
            ) {
                FilledTonalButton(
                    colors = ButtonDefaults.filledTonalButtonColors(),
                    modifier = Modifier.fillMaxWidth(),
                    enabled = formState.isValid && uiState !is UIState.Loading,
                    onClick = {
                        focusRequester.clearFocus()
                        onFormConfirm(
                            hubAddressField.value,
                            hubNameField.value,
                            hubPasswordField.value
                        )
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
