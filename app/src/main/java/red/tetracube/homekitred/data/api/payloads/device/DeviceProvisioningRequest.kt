package red.tetracube.homekitred.data.api.payloads.device

import com.fasterxml.jackson.annotation.JsonCreator
import red.tetracube.homekitred.data.enumerations.DeviceType

data class DeviceProvisioningRequest @JsonCreator constructor(
    val deviceType: DeviceType,
    val deviceName: String,
    val roomSlug: String?,
    val upsProvisioning: UPSProvisioningFields?
)

data class UPSProvisioningFields @JsonCreator constructor(
    val deviceAddress: String,
    val devicePort: Int,
    val internalName: String
)
