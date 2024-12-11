package red.tetracube.homekitred.iot.device.room

import red.tetracube.homekitred.app.exceptions.HomeKitRedError
import red.tetracube.homekitred.data.api.entities.device.DeviceRoomJoin
import red.tetracube.homekitred.data.db.datasource.DeviceDatasource
import red.tetracube.homekitred.data.db.datasource.HubDatasource
import red.tetracube.homekitred.data.db.datasource.RoomDatasource

class DeviceRoomUseCases(
    private val hubDatasource: HubDatasource,
    private val roomDatasource: RoomDatasource,
    private val deviceDatasource: DeviceDatasource,
    private val deviceRoomAPIRepository: DeviceRoomAPIRepository
) {

    suspend fun getRoomsMap(): Map<String, String> {
        val hub = hubDatasource.getActiveHub()!!
        return roomDatasource.getHubRooms(hub.slug)
            .map { room -> room.slug to room.name }
            .toMap()
    }

    suspend fun getDeviceRoom(deviceSlug: String): Pair<String, String?>? {
        return deviceDatasource.getDeviceBySlug(deviceSlug)
            ?.let { it.name to it.roomSlug }
    }

    suspend fun updateDeviceRoom(deviceSlug: String, roomSlug: String): Result<Unit> {
        val hub = hubDatasource.getActiveHub()!!
        try {
            var deviceRoomUpdateResponse = deviceRoomAPIRepository.deviceRoomJoin(
                hub.apiURI,
                hub.token,
                DeviceRoomJoin(
                    deviceSlug,
                    roomSlug
                )
            )
            deviceDatasource.getDeviceBySlug(deviceRoomUpdateResponse.deviceSlug)
                ?.apply {
                    this.roomSlug = deviceRoomUpdateResponse.roomSlug
                    deviceDatasource.updateDevice(this)
                }
            return Result.success(Unit)
        } catch (ex: HomeKitRedError) {
            return Result.failure(ex)
        }
    }

}