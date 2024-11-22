package red.tetracube.homekitred.data.services

import red.tetracube.homekitred.data.api.payloads.device.DeviceTelemetryResponse.UPSTelemetryData
import red.tetracube.homekitred.data.api.repositories.DeviceAPIRepository
import red.tetracube.homekitred.data.db.HomeKitRedDatabase
import red.tetracube.homekitred.data.db.entities.DeviceEntity
import red.tetracube.homekitred.data.mappers.asEntity

class DeviceService(
    private val deviceAPIRepository: DeviceAPIRepository,
    private val database: HomeKitRedDatabase
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
            .forEach { database.deviceRepository().insert(it) }

        database.deviceRepository().getDevicesStaticList(hubSlug)
            .forEach { deviceEntity ->
                var deviceTelemetry =
                    deviceAPIRepository.getDeviceTelemetry(apiURI, token, deviceEntity.slug)

                if (deviceTelemetry is UPSTelemetryData) {
                    var telemetryEntity = deviceTelemetry.asEntity()
                    database.upsTelemetryDatasource().insert(telemetryEntity)
                }
            }
    }

    suspend fun listenDeviceTelemetryStreams(
        streamingHubAddress: String,
        token: String
    ) {
        deviceAPIRepository.getTelemetryStreaming(streamingHubAddress)
            .collect {
                if (it is UPSTelemetryData) {
                    var telemetryEntity = it.asEntity()
                    database.upsTelemetryDatasource().insert(telemetryEntity)
                }
            }
    }

}