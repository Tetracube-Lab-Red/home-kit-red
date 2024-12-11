package red.tetracube.homekitred.data.api.entities.room

import com.fasterxml.jackson.annotation.JsonCreator

data class RoomCreateRequest @JsonCreator constructor(
    val name: String
)
