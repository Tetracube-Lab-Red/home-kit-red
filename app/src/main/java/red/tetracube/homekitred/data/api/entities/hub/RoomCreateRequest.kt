package red.tetracube.homekitred.data.api.entities.hub

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty

data class RoomCreateRequest @JsonCreator constructor(
    @JsonProperty val name: String
)
