package red.tetracube.homekitred.iot.home

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import red.tetracube.homekitred.data.db.datasource.DeviceDatasource
import red.tetracube.homekitred.data.db.datasource.DeviceScanTelemetryDatasource
import red.tetracube.homekitred.data.db.datasource.HubDatasource
import red.tetracube.homekitred.data.db.datasource.RoomDatasource
import red.tetracube.homekitred.data.services.DeviceService
import red.tetracube.homekitred.iot.home.domain.mappers.toDomain
import red.tetracube.homekitred.iot.home.domain.models.Device
import red.tetracube.homekitred.iot.home.domain.models.HubWithRooms
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class IoTHomeUseCases(
    private val hubDatasource: HubDatasource,
    private val deviceService: DeviceService,
    private val deviceDatasource: DeviceDatasource,
    private val roomDatasource: RoomDatasource,
    private val deviceScanTelemetryDatasource: DeviceScanTelemetryDatasource
) {

    fun getHubWithRooms(): Flow<HubWithRooms> =
        hubDatasource.getHubAndRooms()
            .map { it.toDomain() }

    suspend fun getDevices(roomSlug: String?): Flow<Device> {
        val hub = hubDatasource.getActiveHub()!!
        val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm:ss")
            .withZone(ZoneId.systemDefault());
        deviceService.retrieveDevices(hub.slug, hub.apiURI, hub.token)
        return deviceDatasource.getDevices(hub.slug)
            .filter { entity ->
                if (roomSlug == null) {
                    true
                } else {
                    entity.roomSlug == roomSlug
                }
            }
            .map { entity ->
                val connectivityStatus = deviceScanTelemetryDatasource.getLatestByDevice(entity.slug)
                Device(
                    name = entity.name,
                    slug = entity.slug,
                    roomName = entity.roomSlug?.let { roomDatasource.getBySlug(it).name },
                    roomSlug = entity.roomSlug,
                    notifications = 0,
                    status = "",
                    connectionStatus = "${connectivityStatus.connectivity} - ${connectivityStatus.telemetryStatus}",
                    type = entity.type,
                    telemetryTS = formatter.format(connectivityStatus.telemetryTS)
                )
            }
    }

}