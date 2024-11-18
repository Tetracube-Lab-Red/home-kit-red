package red.tetracube.homekitred.data.services

import red.tetracube.homekitred.data.api.repositories.DeviceAPIRepository
import red.tetracube.homekitred.data.db.datasource.DeviceDatasource
import red.tetracube.homekitred.data.db.datasource.DeviceScanTelemetryDatasource
import red.tetracube.homekitred.data.db.entities.DeviceEntity
import red.tetracube.homekitred.data.db.entities.DeviceScanTelemetryEntity

class DeviceService(
    private val deviceAPIRepository: DeviceAPIRepository,
    private val deviceDatasource: DeviceDatasource,
    private val deviceScanTelemetryDatasource: DeviceScanTelemetryDatasource
) {

    suspend fun retrieveDevices(
        hubSlug: String,
        apiURI: String,
        token: String
    ) {
        deviceAPIRepository.getDevices(apiURI, token)
            .devices
            .map { deviceData ->
                DeviceEntity(
                    slug = deviceData.slug,
                    name = deviceData.name,
                    type = deviceData.type,
                    hubSlug = hubSlug,
                    roomSlug = deviceData.roomSlug
                )
            }
            .forEach { deviceDatasource.insert(it) }

        deviceDatasource.getDevicesStaticList(hubSlug)
            .forEach { deviceEntity ->
                var deviceTelemetry =
                    deviceAPIRepository.getDeviceTelemetry(apiURI, token, deviceEntity.slug)
                var deviceScanTelemetryEntity = DeviceScanTelemetryEntity()
                deviceScanTelemetryEntity.deviceSlug = deviceEntity.slug
                deviceScanTelemetryEntity.telemetryTS = deviceTelemetry.timestamp
                deviceScanTelemetryEntity.telemetryStatus = deviceTelemetry.telemetryTransmission
                deviceScanTelemetryEntity.connectivity = deviceTelemetry.connectivity
                deviceScanTelemetryDatasource.insert(deviceScanTelemetryEntity)
            }
    }

}