package red.tetracube.homekitred.ui.iot.home.mappers

import red.tetracube.homekitred.data.db.entities.DeviceEntity
import red.tetracube.homekitred.ui.iot.home.models.BasicTelemetry
import red.tetracube.homekitred.ui.iot.home.models.Device

fun DeviceEntity.toUIModel(roomName: String?, basicTelemetry: BasicTelemetry?) =
    Device(
        id = this.id,
        name = this.name,
        roomName = roomName,
        roomId = this.roomId,
        notifications = 0,
        type = this.type,
        telemetry = basicTelemetry
    )