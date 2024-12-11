package red.tetracube.homekitred.data.api.entities.hub

import com.fasterxml.jackson.annotation.JsonCreator

data class HubCreateResponse @JsonCreator constructor(
    val slug: String,
    val name: String
)
