package red.tetracube.homekitred.business.usecases

import red.tetracube.homekitred.models.errors.HomeKitRedError
import red.tetracube.homekitred.data.api.datasource.HubAPIDataSource
import red.tetracube.homekitred.data.api.entities.hub.RoomCreateRequest
import red.tetracube.homekitred.data.api.entities.hub.RoomData
import red.tetracube.homekitred.data.db.datasource.HubDataSource
import red.tetracube.homekitred.data.db.datasource.RoomDataSource
import red.tetracube.homekitred.data.db.entities.RoomEntity

class RoomUseCases(
    private val hubDataSource: HubDataSource,
    private val hubApiDataSource: HubAPIDataSource,
    private val roomDataSource: RoomDataSource
) {

    suspend fun createRoom(name: String): Result<Unit> {
        val hub = hubDataSource.getActiveHub()!!
        val createRoomAPIResponse = try {
            hubApiDataSource.createRoom(
                hub.apiURI,
                hub.token,
                RoomCreateRequest(name)
            )
        } catch (exception: HomeKitRedError) {
            exception
        }

        if (createRoomAPIResponse is HomeKitRedError) {
            return Result.failure(createRoomAPIResponse)
        }

        val roomData = createRoomAPIResponse as RoomData
        val roomEntity = RoomEntity(
            id = roomData.id,
            name = roomData.name,
            hubId = hub.id
        )
        roomDataSource.insert(roomEntity)
        return Result.success(Unit)
    }

}