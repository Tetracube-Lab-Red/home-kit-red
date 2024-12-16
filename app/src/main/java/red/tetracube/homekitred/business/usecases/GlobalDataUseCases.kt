package red.tetracube.homekitred.business.usecases

import red.tetracube.homekitred.data.api.datasource.HubAPIDataSource
import red.tetracube.homekitred.data.api.datasource.IoTAPIDataSource
import red.tetracube.homekitred.data.db.datasource.DeviceDataSource
import red.tetracube.homekitred.data.db.datasource.RoomDataSource
import red.tetracube.homekitred.data.db.entities.DeviceEntity
import red.tetracube.homekitred.data.db.entities.RoomEntity
import red.tetracube.homekitred.models.HubConnectionInfo
import red.tetracube.homekitred.models.errors.HomeKitRedError

class GlobalDataUseCases(
    private val roomDatasource: RoomDataSource,
    private val hubAPIDataSource: HubAPIDataSource,
    private val ioTAPIDataSource: IoTAPIDataSource,
    private val deviceDataSource: DeviceDataSource
) {

    suspend fun updateLocalData(hubConnectionInfo: HubConnectionInfo): Result<Unit> {
        try {
            hubAPIDataSource.getRooms(hubConnectionInfo.apiURI, hubConnectionInfo.token)
                .rooms
                .map { room ->
                    RoomEntity(
                        id = room.id,
                        name = room.name,
                        hubId = hubConnectionInfo.id
                    )
                }
                .forEach { roomEntity ->
                    roomDatasource.insert(roomEntity)
                }
        } catch (ex: HomeKitRedError) {
            return Result.failure(ex)
        }
        // 2 get notifications
        try {
            ioTAPIDataSource.getDevices(hubConnectionInfo.apiURI, hubConnectionInfo.token)
                .devices
                .map { deviceData ->
                    DeviceEntity(
                        id = deviceData.id,
                        name = deviceData.name,
                        type = deviceData.deviceType,
                        hubId = hubConnectionInfo.id,
                        roomId = deviceData.roomId
                    )
                }
                .forEach { deviceDataSource.insert(it) }
        } catch (ex: HomeKitRedError) {
            return Result.failure(ex)
        }
        return Result.success(Unit)
    }

}