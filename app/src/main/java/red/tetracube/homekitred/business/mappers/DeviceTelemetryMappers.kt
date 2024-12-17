package red.tetracube.homekitred.business.mappers

import red.tetracube.homekitred.business.enumerations.UPSStatus
import red.tetracube.homekitred.data.api.entities.device.DeviceTelemetryResponse.UPSTelemetryData
import red.tetracube.homekitred.data.db.entities.UPSTelemetryEntity

fun UPSTelemetryData.asEntity(): UPSTelemetryEntity =
    UPSTelemetryEntity(
        id = this.id,
        telemetryTS = this.telemetryTS,
        outFrequency = this.outFrequency,
        outVoltage = this.outVoltage,
        outCurrent = this.outCurrent,
        batteryVoltage = this.batteryVoltage,
        batteryRuntime = this.batteryRuntime,
        load = this.load,
        temperature = this.temperature,
        inFrequency = this.inFrequency,
        inVoltage = this.inVoltage,
        powerFactor = this.powerFactor,
        batteryCharge = this.batteryCharge,
        primaryStatus = this.statuses.firstOrNull() ?: UPSStatus.NULL,
        secondaryStatus = this.statuses.lastOrNull(),
        deviceId = this.deviceId,
        connectivityHealth = this.connectivityHealth,
        telemetryHealth = this.telemetryHealth
    )