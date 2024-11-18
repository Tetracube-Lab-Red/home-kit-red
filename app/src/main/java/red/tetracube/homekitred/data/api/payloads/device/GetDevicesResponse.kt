package red.tetracube.homekitred.data.api.payloads.device

import com.fasterxml.jackson.annotation.JsonCreator

data class GetDevicesResponse @JsonCreator constructor(
    val devices: List<DeviceDataItem>
)
