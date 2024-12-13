package red.tetracube.homekitred.business.services

import red.tetracube.homekitred.data.api.entities.device.DeviceTelemetryResponse.UPSTelemetryData
import red.tetracube.homekitred.data.api.datasource.IoTAPIDataSource
import red.tetracube.homekitred.data.db.HomeKitRedDatabase
import red.tetracube.homekitred.data.db.entities.DeviceEntity
import red.tetracube.homekitred.business.mappers.asEntity

class DeviceService(
    private val ioTAPIDataSource: IoTAPIDataSource,
    private val database: HomeKitRedDatabase
) {

    suspend fun retrieveDevices(
        hubSlug: String,
        apiURI: String,
        token: String
    ) {
        ioTAPIDataSource.getDevices(apiURI, token)
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
                    ioTAPIDataSource.getDeviceTelemetry(apiURI, token, deviceEntity.slug)

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
        ioTAPIDataSource.getTelemetryStreaming(streamingHubAddress)
            .collect {
                if (it is UPSTelemetryData) {
                    var telemetryEntity = it.asEntity()
                    database.upsTelemetryDatasource().insert(telemetryEntity)
                }
            }
    }

}