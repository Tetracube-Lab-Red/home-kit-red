package red.tetracube.homekitred.data.api.payloads.hub

import kotlinx.serialization.Serializable

@Serializable
data class HubCreateRequest(
    val name: String,
    val password: String
)