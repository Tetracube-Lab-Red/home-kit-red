package red.tetracube.homekitred.business.usecases

import red.tetracube.homekitred.models.errors.HomeKitRedError
import red.tetracube.homekitred.models.HubConnectionInfo
import red.tetracube.homekitred.data.api.datasource.HubAPIDataSource
import red.tetracube.homekitred.data.db.datasource.RoomDataSource
import red.tetracube.homekitred.data.db.entities.RoomEntity

class GlobalDataUseCases(
    private val roomDatasource: RoomDataSource,
    private val hubAPIDataSource: HubAPIDataSource,
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
        // 3 get devices

        return Result.success(Unit)
    }

}