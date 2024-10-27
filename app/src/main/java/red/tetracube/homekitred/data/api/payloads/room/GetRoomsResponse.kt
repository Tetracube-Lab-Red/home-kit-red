package red.tetracube.homekitred.data.api.payloads.room

import kotlinx.serialization.Serializable

@Serializable
data class GetRoomsResponse(
    val rooms: List<RoomResponse>
)
