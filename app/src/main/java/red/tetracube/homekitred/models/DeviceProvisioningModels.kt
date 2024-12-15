package red.tetracube.homekitred.models

import red.tetracube.homekitred.business.enumerations.DeviceType

data class DeviceProvisioning(
    val name: String,
    val deviceType: DeviceType
)

data class UPSProvisioning(
    val nutServerURI: String,
    val nutServerPort: Int,
    val upsInternalName: String
)