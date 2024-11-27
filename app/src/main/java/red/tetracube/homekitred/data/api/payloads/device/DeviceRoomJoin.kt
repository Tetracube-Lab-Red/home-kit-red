package red.tetracube.homekitred.data.api.payloads.device

import com.fasterxml.jackson.annotation.JsonCreator

data class DeviceRoomJoin @JsonCreator constructor(
    val deviceSlug: String,
    val roomSlug: String
)
