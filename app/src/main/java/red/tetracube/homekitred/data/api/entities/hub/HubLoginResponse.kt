package red.tetracube.homekitred.data.api.entities.hub

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import java.util.UUID

data class HubLoginResponse @JsonCreator constructor(
    @JsonProperty("id") val id: UUID,
    @JsonProperty("name") val name: String,
    @JsonProperty("token") val token: String
)