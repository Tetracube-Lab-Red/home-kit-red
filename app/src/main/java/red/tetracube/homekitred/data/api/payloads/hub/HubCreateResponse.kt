package red.tetracube.homekitred.data.api.payloads.hub

import com.fasterxml.jackson.annotation.JsonCreator

data class HubCreateResponse @JsonCreator constructor(
    val slug: String,
    val name: String
)
