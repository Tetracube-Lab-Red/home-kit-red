package red.tetracube.homekitred.data.api.payloads.room

import kotlinx.serialization.Serializable

@Serializable
data class RoomCreateRequest(
    val name: String
)
