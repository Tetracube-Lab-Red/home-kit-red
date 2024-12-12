package red.tetracube.homekitred.iot.device.provisioning.models

import red.tetracube.homekitred.business.enumerations.DeviceType

data class DeviceProvisioningFormModel(
    val deviceName: DeviceNameField = DeviceNameField(),
    val deviceType: DeviceTypeField = DeviceTypeField(),
    val formIsValid: Boolean = false
)

data class DeviceNameField(
    val isDirty: Boolean = false,
    val isValid: Boolean = false,
    val validationMessage: String = "Required",
    val value: String = ""
)

data class DeviceTypeField(
    val isDirty: Boolean = false,
    val isValid: Boolean = false,
    val validationMessage: String = "Required",
    val internalType: DeviceType = DeviceType.NONE,
    val value: String = ""
)

data class UPSProvisioningFormModel(
    val nutServerHost: NutServerHost = NutServerHost(),
    val nutServerPort: NutServerPort = NutServerPort(),
    val nutUPSAlias: NutUPSAlias = NutUPSAlias()
)

data class NutServerHost(
    val isDirty: Boolean = false,
    val isValid: Boolean = false,
    val validationMessage: String = "Required",
    val value: String = ""
)

data class NutServerPort(
    val isDirty: Boolean = false,
    val isValid: Boolean = false,
    val validationMessage: String = "Required",
    val value: String = ""
)

data class NutUPSAlias(
    val isDirty: Boolean = false,
    val isValid: Boolean = false,
    val validationMessage: String = "Required",
    val value: String = ""
)