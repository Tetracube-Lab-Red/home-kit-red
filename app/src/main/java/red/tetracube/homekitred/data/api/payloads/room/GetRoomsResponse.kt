package red.tetracube.homekitred.data.api.payloads.room

import com.fasterxml.jackson.annotation.JsonCreator

data class GetRoomsResponse @JsonCreator constructor(
    val rooms: List<RoomResponse>
)
