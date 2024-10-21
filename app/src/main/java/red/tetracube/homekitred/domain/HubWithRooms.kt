package red.tetracube.homekitred.domain

data class HubWithRooms(
    val slug: String,
    val avatarName: String,
    val name: String,
    val rooms: List<Room>
)
