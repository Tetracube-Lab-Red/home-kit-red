package red.tetracube.homekitred.iot.device.room

import red.tetracube.homekitred.data.db.datasource.DeviceDatasource
import red.tetracube.homekitred.data.db.datasource.HubDatasource
import red.tetracube.homekitred.data.db.datasource.RoomDatasource

class DeviceRoomUseCases(
    private val hubDatasource: HubDatasource,
    private val roomDatasource: RoomDatasource,
    private val deviceDatasource: DeviceDatasource
) {

    suspend fun getRoomsMap(): Map<String, String> {
        val hub = hubDatasource.getActiveHub()!!
        return roomDatasource.getHubRooms(hub.slug)
            .map { room -> room.slug to room.name }
            .toMap()
    }

    suspend fun getDeviceRoom(deviceSlug: String): String? {
        return deviceDatasource.getDeviceBySlug(deviceSlug)
            ?.roomSlug
    }

}