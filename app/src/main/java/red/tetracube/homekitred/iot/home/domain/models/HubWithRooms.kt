package red.tetracube.homekitred.iot.home.domain.models

import red.tetracube.homekitred.domain.Room

data class HubWithRooms(
    val slug: String,
    val avatarName: String,
    val name: String,
    val rooms: List<Room>
)
