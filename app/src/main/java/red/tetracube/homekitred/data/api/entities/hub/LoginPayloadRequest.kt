package red.tetracube.homekitred.data.api.entities.hub

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty

data class LoginPayloadRequest @JsonCreator constructor(
    @JsonProperty("name") val name: String,
    @JsonProperty("accessCode") val accessCode: String
)