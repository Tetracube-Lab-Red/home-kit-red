package red.tetracube.homekitred.iot.home

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import red.tetracube.homekitred.data.db.HomeKitRedDatabase
import red.tetracube.homekitred.data.db.datasource.HubDatasource
import red.tetracube.homekitred.data.services.DeviceService
import red.tetracube.homekitred.iot.home.domain.mappers.toDomain
import red.tetracube.homekitred.iot.home.domain.models.Device
import red.tetracube.homekitred.iot.home.domain.models.HubWithRooms
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class IoTHomeUseCases(
    private val hubDatasource: HubDatasource,
    private val deviceService: DeviceService,
    private val database: HomeKitRedDatabase
) {

    fun getHubWithRooms(): Flow<HubWithRooms> =
        hubDatasource.getHubAndRooms()
            .map { it.toDomain() }

    suspend fun getDevices(roomSlug: String?): Flow<Device> {
        val hub = hubDatasource.getActiveHub()!!
        val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm:ss")
            .withZone(ZoneId.systemDefault());
        deviceService.retrieveDevices(hub.slug, hub.apiURI, hub.token)
        return database.deviceRepository().getDevices(hub.slug)
            .filter { entity ->
                if (roomSlug == null) {
                    true
                } else {
                    entity.roomSlug == roomSlug
                }
            }
            .map { entity ->
                val connectivityStatus =
                    database.deviceScanTelemetryDatasource().getLatestByDevice(entity.slug)
                var telemetry =
                    database.upsTelemetryDatasource().getLatest(entity.slug)
                Device(
                    name = entity.name,
                    slug = entity.slug,
                    roomName = entity.roomSlug?.let {
                        database.roomRepository().getBySlug(it).name
                    },
                    roomSlug = entity.roomSlug,
                    notifications = 0,
                    status = telemetry.primaryStatus.name + (telemetry.secondaryStatus?.let { " - ${it.name}" } ?: ""),
                    connectionStatus = "${connectivityStatus.connectivity} - ${connectivityStatus.telemetryStatus}",
                    type = entity.type,
                    telemetryTS = formatter.format(connectivityStatus.telemetryTS)
                )
            }
    }

}