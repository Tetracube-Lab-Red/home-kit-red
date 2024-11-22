package red.tetracube.homekitred.iot.home.domain.models

import red.tetracube.homekitred.data.enumerations.DeviceType

data class Device (
    val slug: String,
    val name: String,
    val roomName: String?,
    val roomSlug: String?,
    val notifications: Int,
    val type: DeviceType
)