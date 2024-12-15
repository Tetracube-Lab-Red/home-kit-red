package red.tetracube.homekitred.business.usecases

import red.tetracube.homekitred.data.api.datasource.IoTAPIDataSource
import red.tetracube.homekitred.data.api.entities.device.DeviceRoomJoin
import red.tetracube.homekitred.data.db.datasource.DeviceDataSource
import red.tetracube.homekitred.data.db.datasource.HubDataSource
import red.tetracube.homekitred.data.db.datasource.RoomDataSource
import red.tetracube.homekitred.models.RoomSelectItem
import red.tetracube.homekitred.models.errors.HomeKitRedError
import java.util.UUID

class DeviceRoomUseCases(
    private val hubDataSource: HubDataSource,
    private val deviceDataSource: DeviceDataSource,
    private val roomDataSource: RoomDataSource,
    private val ioTAPIDataSource: IoTAPIDataSource
) {

    suspend fun getRoomsForDeviceSelect(deviceId: UUID): List<RoomSelectItem> {
        val device = deviceDataSource.getDeviceById(deviceId)
        return hubDataSource.getActiveHub()?.let {
            roomDataSource.getHubRooms(it.id)
        }
            ?.map { room ->
                RoomSelectItem(
                    roomId = room.id,
                    roomName = room.name,
                    selected = device != null && device.roomId == room.id
                )
            }
            ?: emptyList()
    }

    suspend fun updateDeviceRoom(deviceId: UUID, roomId: UUID): Result<Unit> {
        val hub = hubDataSource.getActiveHub()!!
        try {
            var deviceRoomUpdateResponse = ioTAPIDataSource.deviceRoomJoin(
                hub.apiURI,
                hub.token,
                DeviceRoomJoin(
                    deviceId,
                    roomId
                )
            )
            deviceDataSource.getDeviceById(deviceRoomUpdateResponse.deviceId)
                ?.apply {
                    this.roomId = deviceRoomUpdateResponse.roomId
                    deviceDataSource.updateDevice(this)
                }
            return Result.success(Unit)
        } catch (ex: HomeKitRedError) {
            return Result.failure(ex)
        }
    }
}