package red.tetracube.homekitred.data.api.entities.hub

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import java.util.UUID

data class RoomData @JsonCreator constructor(
    @JsonProperty val id: UUID,
    @JsonProperty val name: String
)
