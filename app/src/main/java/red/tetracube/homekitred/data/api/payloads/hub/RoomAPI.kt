package red.tetracube.homekitred.data.api.payloads.hub

import kotlinx.serialization.Serializable

@Serializable
data class RoomAPI(
    val slug: String,
    val name: String
)
