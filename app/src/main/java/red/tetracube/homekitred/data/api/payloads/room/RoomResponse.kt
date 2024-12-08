package red.tetracube.homekitred.data.api.payloads.room

import com.fasterxml.jackson.annotation.JsonCreator

data class RoomResponse @JsonCreator constructor(
    val slug: String,
    val name: String
)
