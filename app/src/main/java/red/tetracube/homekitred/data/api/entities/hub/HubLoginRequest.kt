package red.tetracube.homekitred.data.api.entities.hub

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty

data class HubLoginRequest @JsonCreator constructor(
    @JsonProperty("name") val slug: String,
    @JsonProperty("accessCode") val accessCode: String
)
