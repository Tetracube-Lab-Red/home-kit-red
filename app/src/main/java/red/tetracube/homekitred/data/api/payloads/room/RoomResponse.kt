package red.tetracube.homekitred.data.api.payloads.room

import kotlinx.serialization.Serializable

@Serializable
data class RoomResponse(
    val slug: String,
    val name: String
)
