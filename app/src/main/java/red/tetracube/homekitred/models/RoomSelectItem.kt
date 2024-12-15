package red.tetracube.homekitred.models

import java.util.UUID

data class RoomSelectItem (
    val roomId: UUID,
    val roomName: String,
    val selected: Boolean
)