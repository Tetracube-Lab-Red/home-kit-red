package red.tetracube.homekitred.ui.iot.home.models

import java.util.UUID

data class IoTDashboardModel(
    val hubId: UUID,
    val hubName: String,
    val avatarName: String,
    val rooms: List<Room>
)

data class Room(
    val id: UUID?,
    val name: String
)