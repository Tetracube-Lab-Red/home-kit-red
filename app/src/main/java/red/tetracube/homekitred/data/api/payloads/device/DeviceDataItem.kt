package red.tetracube.homekitred.data.api.payloads.device

import com.fasterxml.jackson.annotation.JsonCreator
import red.tetracube.homekitred.data.enumerations.DeviceType

data class DeviceDataItem @JsonCreator constructor(
    val id: String,
    val type: DeviceType,
    val slug: String,
    val name: String,
    val roomSlug: String?
)
