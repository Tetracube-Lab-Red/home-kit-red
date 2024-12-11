package red.tetracube.homekitred.data.api.entities.device

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import red.tetracube.homekitred.data.enumerations.DeviceType
import red.tetracube.homekitred.data.enumerations.ProvisioningStatus
import java.util.UUID

data class DeviceData @JsonCreator constructor(
    @JsonProperty val id: UUID,
    @JsonProperty val deviceType: DeviceType,
    @JsonProperty val name: String,
    @JsonProperty val roomId: UUID?,
    @JsonProperty val provisioningStatus: ProvisioningStatus
)
