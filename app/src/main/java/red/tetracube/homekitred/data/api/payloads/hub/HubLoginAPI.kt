package red.tetracube.homekitred.data.api.payloads.hub

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty

data class HubLoginAPI @JsonCreator constructor(
    @JsonProperty("slug") val slug: String,
    @JsonProperty("name") val name: String,
    @JsonProperty("token") val token: String
)
