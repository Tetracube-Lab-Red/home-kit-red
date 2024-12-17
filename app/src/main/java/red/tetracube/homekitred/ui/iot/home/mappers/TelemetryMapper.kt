package red.tetracube.homekitred.ui.iot.home.mappers

import red.tetracube.homekitred.data.db.entities.UPSTelemetryEntity
import red.tetracube.homekitred.ui.iot.home.models.BasicTelemetry

fun UPSTelemetryEntity.asBasicTelemetry() =
    BasicTelemetry.UPSBasicTelemetry(
        id = this.id,
        deviceId = this.deviceId,
        primaryStatus = this.primaryStatus,
        secondaryStatus = this.secondaryStatus,
        connectivityHealth = this.connectivityHealth,
        telemetryHealth = this.telemetryHealth,
        telemetryTS = this.telemetryTS
    )