package red.tetracube.homekitred.business.services

import red.tetracube.homekitred.data.api.entities.device.DeviceTelemetryResponse.UPSTelemetryData
import red.tetracube.homekitred.data.api.datasource.IoTAPIDataSource
import red.tetracube.homekitred.data.db.HomeKitRedDatabase
import red.tetracube.homekitred.data.db.entities.DeviceEntity
import red.tetracube.homekitred.business.mappers.asEntity
import java.util.UUID

class DeviceService(
    private val ioTAPIDataSource: IoTAPIDataSource,
    private val database: HomeKitRedDatabase
) {

    suspend fun retrieveDevices(
        hubId: UUID,
        apiURI: String,
        token: String
    ) {
        ioTAPIDataSource.getDevices(apiURI, token)
            .devices
            .map { deviceData ->
                DeviceEntity(
                    id = deviceData.id,
                    name = deviceData.name,
                    type = deviceData.deviceType,
                    hubId = hubId,
                    roomId = deviceData.roomId
                )
            }
            .forEach { database.deviceDataSource().insert(it) }

        database.deviceDataSource().getDevicesStaticList(hubSlug)
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