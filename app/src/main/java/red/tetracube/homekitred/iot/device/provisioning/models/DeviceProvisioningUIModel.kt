package red.tetracube.homekitred.iot.device.provisioning.models

import red.tetracube.homekitred.data.enumerations.DeviceType

data class DeviceProvisioningUIModel(
    val deviceName: DeviceNameField = DeviceNameField(),
    val deviceType: DeviceTypeField = DeviceTypeField(),
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