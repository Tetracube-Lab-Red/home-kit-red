package red.tetracube.homekitred.iot.home.domain.models

data class HubWithRooms(
    val slug: String,
    val avatarName: String,
    val name: String,
    val rooms: List<Room>
)
