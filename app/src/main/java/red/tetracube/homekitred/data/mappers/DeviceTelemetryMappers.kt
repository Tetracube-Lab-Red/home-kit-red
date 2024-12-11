package red.tetracube.homekitred.data.mappers

import red.tetracube.homekitred.data.api.entities.device.DeviceTelemetryResponse.UPSTelemetryData
import red.tetracube.homekitred.data.db.entities.UPSTelemetryEntity
import red.tetracube.homekitred.data.enumerations.UPSStatus
import red.tetracube.homekitred.iot.home.domain.models.BasicTelemetry

fun UPSTelemetryData.asEntity(): UPSTelemetryEntity {
    val entity = UPSTelemetryEntity()
    entity.telemetryTS = this.telemetryTS
    entity.outFrequency = this.outFrequency
    entity.outVoltage = this.outVoltage
    entity.outCurrent = this.outCurrent
    entity.batteryVoltage = this.batteryVoltage
    entity.batteryRuntime = this.batteryRuntime
    entity.load = this.load
    entity.temperature = this.temperature
    entity.inFrequency = this.inFrequency
    entity.inVoltage = this.inVoltage
    entity.powerFactor = this.powerFactor
    entity.batteryCharge = this.batteryCharge
    entity.primaryStatus = this.statuses.firstOrNull() ?: UPSStatus.NULL
    entity.secondaryStatus = this.statuses.lastOrNull()
    entity.deviceSlug = this.deviceSlug
    entity.connectivityHealth = this.connectivityHealth
    entity.telemetryHealth = this.telemetryHealth
    return entity
}

fun UPSTelemetryEntity.asBasicTelemetry() : BasicTelemetry.UPSBasicTelemetry {
    return  BasicTelemetry.UPSBasicTelemetry(
        deviceSlug = this.deviceSlug,
        primaryStatus = this.primaryStatus,
        secondaryStatus = this.secondaryStatus,
        connectivityHealth = this.connectivityHealth,
        telemetryHealth = this.telemetryHealth,
        telemetryTS = this.telemetryTS
    )
}