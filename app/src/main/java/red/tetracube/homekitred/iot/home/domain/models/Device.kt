package red.tetracube.homekitred.iot.home.domain.models

import red.tetracube.homekitred.business.enumerations.DeviceType
import java.util.UUID

data class Device (
    val id: UUID,
    val name: String,
    val roomName: String?,
    val roomId: UUID?,
    val notifications: Int,
    val type: DeviceType
)