package red.tetracube.homekitred.data.api.entities.hub

import com.fasterxml.jackson.annotation.JsonCreator

data class GetRoomsResponse @JsonCreator constructor(
    val rooms: List<RoomData>
)
