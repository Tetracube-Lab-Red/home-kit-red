package red.tetracube.homekitred.data.api.entities.device

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import java.util.UUID

data class DeviceRoomJoin @JsonCreator constructor(
    @JsonProperty val deviceId: UUID,
    @JsonProperty val roomId: UUID
)
