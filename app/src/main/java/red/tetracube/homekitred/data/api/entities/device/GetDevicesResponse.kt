package red.tetracube.homekitred.data.api.entities.device

import com.fasterxml.jackson.annotation.JsonCreator

data class GetDevicesResponse @JsonCreator constructor(
    val devices: List<DeviceData>
)
