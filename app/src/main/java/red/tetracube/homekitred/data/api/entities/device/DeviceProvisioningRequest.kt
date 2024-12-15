package red.tetracube.homekitred.data.api.entities.device

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import red.tetracube.homekitred.business.enumerations.DeviceType
import java.util.UUID

data class DeviceProvisioningRequest @JsonCreator constructor(
    @JsonProperty val deviceType: DeviceType,
    @JsonProperty val deviceName: String,
    @JsonProperty val roomId: UUID?,
    @JsonProperty val upsProvisioning: UPSProvisioningFields? = null
)

data class UPSProvisioningFields @JsonCreator constructor(
    @JsonProperty val deviceAddress: String,
    @JsonProperty val devicePort: Int,
    @JsonProperty val internalName: String
)
