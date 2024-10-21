package red.tetracube.homekitred.data.api.payloads.hub

import kotlinx.serialization.Serializable

@Serializable
data class HubBase(
    val slug: String,
    val name: String
)
