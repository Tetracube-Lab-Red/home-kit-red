package red.tetracube.homekitred.data.api.payloads.device

import kotlinx.serialization.Serializable
import red.tetracube.homekitred.data.enumerations.DeviceType

@Serializable
data class DeviceProvisioningRequest(
    val deviceType: DeviceType,
    val deviceName: String,
    val roomSlug: String?,
    val upsProvisioning: UPSProvisioningFields?
)

@Serializable
data class UPSProvisioningFields(
    val deviceAddress: String,
    val devicePort: Int,
    val internalName: String
)
