package red.tetracube.homekitred.ui.iot.device.provisioning.models

import red.tetracube.homekitred.business.enumerations.DeviceType

data class DeviceTypeOptionModel(
    val deviceType: DeviceType,
    val fieldValue: String,
    val fieldIcon: Int
)
