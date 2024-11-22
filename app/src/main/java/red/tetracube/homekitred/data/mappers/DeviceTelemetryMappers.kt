package red.tetracube.homekitred.data.mappers

import red.tetracube.homekitred.data.api.payloads.device.DeviceTelemetryResponse.UPSTelemetryData
import red.tetracube.homekitred.data.db.entities.UPSTelemetryEntity
import red.tetracube.homekitred.data.enumerations.UPSStatus

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