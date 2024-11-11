package red.tetracube.homekitred.iot.device.provisioning

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import red.tetracube.homekitred.R
import red.tetracube.homekitred.data.enumerations.DeviceType
import red.tetracube.homekitred.iot.device.provisioning.models.DeviceTypeOptionModel

@Composable
fun DeviceProvisioningScreen(
    viewModel: DeviceProvisioningViewModel,
    navHostController: NavHostController
) {
    val options = DeviceType.entries.map {
        when (it) {
            DeviceType.NONE -> DeviceTypeOptionModel(DeviceType.NONE, "", null)
            DeviceType.UPS -> DeviceTypeOptionModel(
                DeviceType.UPS,
                "UPS",
                R.drawable.battery_charging_full_24px
            )

            DeviceType.SWITCH -> DeviceTypeOptionModel(
                DeviceType.SWITCH,
                "Switch",
                R.drawable.switch_24px
            )

            DeviceType.HUE -> DeviceTypeOptionModel(
                DeviceType.HUE,
                "Hue Lights",
                R.drawable.emoji_objects_24px
            )
        }
    }
        .filter { it.deviceType != DeviceType.NONE }

    DeviceProvisioningScreenUI(
        deviceTypes = options,
        onBackIconClick = {
            navHostController.popBackStack()
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeviceProvisioningScreenUI(
    deviceTypes: List<DeviceTypeOptionModel>,
    onBackIconClick: () -> Unit
) {
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
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
        ) {
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Hub host address") },
                value = "",
                onValueChange = { value: String ->
                    /* onTextInput(
                         FieldInputEvent.FieldName.HUB_ADDRESS,
                         value
                     )*/
                },
                singleLine = true,
                maxLines = 1,
                supportingText = {
                    //Text(formStatus.hubAddressField.validationMessage)
                    Text("Device name")
                },
                //isError = formStatus.hubAddressField.isDirty && !formStatus.hubAddressField.isValid,
                isError = false
            )

            Spacer(modifier = Modifier.size(16.dp))

            var expanded by remember { mutableStateOf(false) }
            var text by remember { mutableStateOf<String>("") }
            var icon by remember { mutableIntStateOf(R.drawable.home_iot_device_24px) }

            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = it },
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
                    value = text,
                    onValueChange = {},
                    readOnly = true,
                    singleLine = true,
                    label = { Text("Device Type") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                )
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                ) {
                    deviceTypes.forEach { option ->
                        DropdownMenuItem(
                            leadingIcon = {
                                Icon(
                                    painter = painterResource(option.fieldIcon!!),
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
                                text = option.fieldValue
                                icon = option.fieldIcon!!
                                expanded = false
                            },
                            contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                        )
                    }
                }
            }

        }
    }
}