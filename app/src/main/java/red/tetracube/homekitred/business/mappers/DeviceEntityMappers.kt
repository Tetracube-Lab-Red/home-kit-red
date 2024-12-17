package red.tetracube.homekitred.business.mappers

import red.tetracube.homekitred.data.api.entities.device.DeviceData
import red.tetracube.homekitred.data.db.entities.DeviceEntity
import java.util.UUID

fun DeviceData.asEntity(hubId: UUID) =
    DeviceEntity(
        id = this.id,
        name = this.name,
        type = this.deviceType,
        hubId = hubId,
        roomId = this.roomId
    )