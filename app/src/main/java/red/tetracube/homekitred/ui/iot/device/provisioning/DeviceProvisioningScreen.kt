package red.tetracube.homekitred.ui.iot.device.provisioning

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import kotlinx.coroutines.launch
import red.tetracube.homekitred.R
import red.tetracube.homekitred.business.enumerations.DeviceType
import red.tetracube.homekitred.models.errors.HomeKitRedError
import red.tetracube.homekitred.ui.state.UIState
import red.tetracube.homekitred.ui.state.form.SelectField
import red.tetracube.homekitred.ui.state.form.TextField
import red.tetracube.homekitred.ui.iot.device.provisioning.models.DeviceTypeOptionModel
import red.tetracube.homekitred.ui.state.form.rememberFormState
import red.tetracube.homekitred.ui.state.form.rememberSelectField
import red.tetracube.homekitred.ui.state.form.rememberTextField
import red.tetracube.homekitred.models.DeviceProvisioning
import red.tetracube.homekitred.models.UPSProvisioning
import kotlin.String

@Composable
fun DeviceProvisioningScreen(
    viewModel: DeviceProvisioningViewModel,
    navHostController: NavHostController
) {
    val uiState = viewModel.uiState.collectAsState().value
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(uiState) {
        if (uiState is UIState.FinishedWithError<*>) {
            val message = when (uiState.error) {
                HomeKitRedError.Conflict -> "There is another device registered with the same name"
                HomeKitRedError.ModuleNotAvailable -> "The module to handle this type of device is not enabled on the hub"
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
            navHostController.popBackStack()
        }
    }

    val options = remember {
        DeviceType.entries.map {
            when (it) {
                DeviceType.NONE -> DeviceTypeOptionModel(
                    DeviceType.NONE,
                    "",
                    R.drawable.home_iot_device_24px
                )

                DeviceType.UPS -> DeviceTypeOptionModel(
                    DeviceType.UPS,
                    "UPS",
                    R.drawable.battery_charging_full_24px
                )
            }
        }
            .filter { it.deviceType != DeviceType.NONE }
    }

    val deviceName = rememberTextField { validateDeviceName(it) }
    val deviceType = rememberSelectField<DeviceType> { validateDeviceType(it) }
    val nutServerURI = rememberTextField { validateNutHost(it) }
    val nutServerPort = rememberTextField { validateNutPort(it) }
    val upsInternalName = rememberTextField { validateNutUPSAlias(it) }

    DeviceProvisioningScreenUI(
        uiState = uiState,
        deviceTypes = options,
        deviceName,
        deviceType,
        onSaveClick = {
            viewModel.onSaveClick(
                DeviceProvisioning(deviceName.value.value, DeviceType.valueOf(deviceType.value.value)),
                UPSProvisioning(
                    nutServerURI.value.value,
                    nutServerPort.value.value.toInt(),
                    upsInternalName.value.value
                )
            )
        },
        onBackIconClick = {
            navHostController.popBackStack()
        },
        snackbarHostState = snackbarHostState,
        upsProvisioningForm = {
            UPSForm(nutServerURI, nutServerPort, upsInternalName)
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeviceProvisioningScreenUI(
    uiState: UIState,
    deviceTypes: List<DeviceTypeOptionModel>,
    deviceName: TextField,
    deviceType: SelectField<DeviceType>,
    onBackIconClick: () -> Unit,
    upsProvisioningForm: @Composable () -> Unit,
    onSaveClick: () -> Unit,
    snackbarHostState: SnackbarHostState
) {
    val formState = rememberFormState(listOf(deviceName, deviceType))

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Device provisioning"
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = onBackIconClick
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.arrow_back_ios_24px),
                            contentDescription = null
                        )
                    }
                },
                actions = {
                    if (uiState is UIState.Loading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp)
                        )
                    } else {
                        TextButton(
                            enabled = formState.isValid,
                            onClick = onSaveClick
                        ) {
                            Text("Save")
                        }
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
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
        ) {
            Text(
                "Device general data",
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.size(16.dp))

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Device name") },
                value = deviceName.value.value,
                onValueChange = { value: String -> deviceName.setValue(value) },
                singleLine = true,
                maxLines = 1,
                supportingText = {
                    deviceName.message.value?.let {
                        Text(it)
                    }
                },
                isError = deviceName.hasError()
            )

            Spacer(modifier = Modifier.size(16.dp))

            var icon by remember { mutableIntStateOf(R.drawable.home_iot_device_24px) }

            ExposedDropdownMenuBox(
                expanded = deviceType.expanded.value,
                onExpandedChange = { deviceType.toggleSelect() },
            ) {
                OutlinedTextField(
                    leadingIcon = {
                        Icon(
                            painter = painterResource(icon),
                            contentDescription = null
                        )
                    },
                    modifier = Modifier
                        .menuAnchor(MenuAnchorType.PrimaryNotEditable)
                        .fillMaxWidth(),
                    value = deviceType.value.value,
                    onValueChange = {},
                    readOnly = true,
                    singleLine = true,
                    supportingText = {
                        deviceType.message.value?.let {
                            Text(it)
                        }
                    },
                    label = { Text("Device Type") },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = deviceType.expanded.value)
                    },
                )
                ExposedDropdownMenu(
                    expanded = deviceType.expanded.value,
                    onDismissRequest = { deviceType.toggleSelect() },
                ) {
                    deviceTypes.forEach { option ->
                        DropdownMenuItem(
                            leadingIcon = {
                                Icon(
                                    painter = painterResource(option.fieldIcon),
                                    contentDescription = null
                                )
                            },
                            text = {
                                Text(
                                    option.fieldValue,
                                    style = MaterialTheme.typography.bodyLarge
                                )
                            },
                            onClick = {
                                icon = option.fieldIcon
                                deviceType.setOptionValue(option.deviceType)
                                deviceType.toggleSelect()
                            },
                            contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.size(16.dp))

            HorizontalDivider()

            Spacer(modifier = Modifier.size(16.dp))

            val density = LocalDensity.current
            AnimatedVisibility(
                visible = deviceType.option.value == DeviceType.UPS,
                enter = slideInVertically {
                    with(density) { 10.dp.roundToPx() }
                } + expandVertically(
                    expandFrom = Alignment.Bottom
                ) + fadeIn(
                    initialAlpha = 0.3f
                ),
                exit = slideOutVertically() + shrinkVertically() + fadeOut()
            ) {
                upsProvisioningForm()
            }
        }
    }
}

@Composable
fun UPSForm(
    nutServerURI: TextField,
    nutServerPort: TextField,
    upsInternalName: TextField
) {
    Column {
        Text(
            "NUT server details",
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(modifier = Modifier.size(16.dp))
        Row() {
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(0.65f),
                label = { Text("Host") },
                value = nutServerURI.value.value,
                onValueChange = { value: String -> nutServerURI.setValue(value) },
                singleLine = true,
                maxLines = 1,
                supportingText = {
                    nutServerURI.message.value?.let {
                        Text(it)
                    }
                },
                isError = nutServerURI.hasError()
            )

            Spacer(modifier = Modifier.size(16.dp))

            OutlinedTextField(
                label = { Text("Port") },
                value = nutServerPort.value.value,
                onValueChange = { value: String -> nutServerPort.setValue(value) },
                singleLine = true,
                maxLines = 1,
                supportingText = {
                    nutServerPort.message.value?.let {
                        Text(it)
                    }
                },
                isError = nutServerPort.hasError(),
                keyboardOptions = KeyboardOptions.Default.copy(
                    autoCorrectEnabled = false,
                    keyboardType = KeyboardType.Number
                )
            )
        }
        Spacer(modifier = Modifier.size(16.dp))
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Alias") },
            value = upsInternalName.value.value,
            onValueChange = { value: String -> upsInternalName.setValue(value) },
            singleLine = true,
            maxLines = 1,
            supportingText = {
                upsInternalName.message.value?.let {
                    Text(it)
                }
            },
            isError = upsInternalName.hasError()
        )
    }
}