package red.tetracube.homekitred.iot.device.room

import red.tetracube.homekitred.models.errors.HomeKitRedError
import red.tetracube.homekitred.data.api.entities.device.DeviceRoomJoin
import red.tetracube.homekitred.data.db.datasource.DeviceDataSource
import red.tetracube.homekitred.data.db.datasource.HubDataSource
import red.tetracube.homekitred.data.db.datasource.RoomDataSource

class DeviceRoomUseCases(
    private val hubDatasource: HubDataSource,
    private val roomDatasource: RoomDataSource,
    private val deviceDatasource: DeviceDataSource,
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