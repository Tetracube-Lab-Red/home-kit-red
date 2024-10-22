package red.tetracube.homekitred.data.api.payloads.room

import kotlinx.serialization.Serializable

@Serializable
data class RoomCreateResponse(
    val slug: String,
    val name: String
)
