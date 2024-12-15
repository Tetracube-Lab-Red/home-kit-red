package red.tetracube.homekitred.iot.home.domain.models

import java.util.UUID

data class HubWithRooms(
    val id: UUID,
    val avatarName: String,
    val name: String,
    val rooms: List<Room>
)
