package red.tetracube.homekitred.iot.home

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import red.tetracube.homekitred.data.db.HomeKitRedDatabase
import red.tetracube.homekitred.data.db.datasource.HubDataSource
import red.tetracube.homekitred.business.mappers.asBasicTelemetry
import red.tetracube.homekitred.business.usecases.DeviceUseCases
import red.tetracube.homekitred.iot.home.domain.mappers.toDomain
import red.tetracube.homekitred.iot.home.domain.models.BasicTelemetry
import red.tetracube.homekitred.iot.home.domain.models.Device
import red.tetracube.homekitred.iot.home.domain.models.HubWithRooms

class IoTHomeUseCases(
    private val hubDatasource: HubDataSource,
    private val deviceUseCases: DeviceUseCases,
    private val database: HomeKitRedDatabase
) {

   /* suspend fun loadData(): Flow<HubWithRooms> {
        val hub = hubDatasource.getActiveHub()!!
       // deviceService.retrieveDevices(hub.slug, hub.apiURI, hub.token)
        return hubDatasource.getHubAndRooms()
            .map { it.toDomain() }
    }

    suspend fun listenAPITelemetrySteaming() {
        val hub = hubDatasource.getActiveHub()!!
       // deviceService.listenDeviceTelemetryStreams(hub.websocketURI, hub.token)
    }

    suspend fun getLatestTelemetry(deviceSlug: String): BasicTelemetry {
        return database.upsTelemetryDatasource().getLatestTelemetry(deviceSlug).asBasicTelemetry()
    }

    fun listenDatabaseTelemetryStreaming(): Flow<List<BasicTelemetry>> {
        var upsTelemetryFlow = database.upsTelemetryDatasource().getLatestTelemetriesStream()
            .map { telemetries ->
                telemetries.map { telemetry ->
                    telemetry.asBasicTelemetry()
                }
            }
        return merge(upsTelemetryFlow)
    }

    suspend fun getDevices(roomSlug: String?): Flow<Device> {
        val hub = hubDatasource.getActiveHub()!!
        return database.deviceRepository().getDevices(hub.slug)
            .filter { entity ->
                if (roomSlug == null) true else entity.roomSlug == roomSlug
            }
            .map { entity ->
                Device(
                    name = entity.name,
                    slug = entity.slug,
                    roomName = entity.roomSlug?.let {
                        database.roomRepository().getBySlug(it).name
                    },
                    roomSlug = entity.roomSlug,
                    notifications = 0,
                    type = entity.type
                )
            }
    }*/

}