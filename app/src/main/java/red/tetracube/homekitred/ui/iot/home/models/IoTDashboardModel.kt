package red.tetracube.homekitred.ui.iot.home.models

import red.tetracube.homekitred.business.enumerations.DeviceType
import java.util.UUID

data class IoTDashboardModel(
    val hubId: UUID,
    val hubName: String,
    val avatarName: String,
    val rooms: List<Room>,
    val list: List<String>
)

data class Room(
    val id: UUID?,
    val name: String
)

data class Device (
    val id: UUID,
    val name: String,
    val roomName: String?,
    val roomId: UUID?,
    val notifications: Int,
    val type: DeviceType
)