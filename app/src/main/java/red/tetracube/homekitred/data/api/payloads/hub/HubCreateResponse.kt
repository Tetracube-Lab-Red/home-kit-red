package red.tetracube.homekitred.data.api.payloads.hub

import kotlinx.serialization.Serializable

@Serializable
data class HubCreateResponse(
    val slug: String,
    val name: String
)
