package red.tetracube.homekitred.data.api.entities.hub

import com.fasterxml.jackson.annotation.JsonCreator

data class HubDetailsAPI @JsonCreator constructor(
    val slug: String,
    val name: String,
    val rooms: List<RoomAPI>
)
