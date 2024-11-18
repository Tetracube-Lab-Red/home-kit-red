package red.tetracube.homekitred.data.api.payloads.room

import com.fasterxml.jackson.annotation.JsonCreator

data class RoomCreateRequest @JsonCreator constructor(
    val name: String
)
