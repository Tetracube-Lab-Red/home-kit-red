package red.tetracube.homekitred.data.api.payloads.hub

import com.fasterxml.jackson.annotation.JsonCreator

data class HubCreateRequest @JsonCreator constructor(
    val name: String,
    val password: String
)