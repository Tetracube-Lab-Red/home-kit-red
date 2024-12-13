package red.tetracube.homekitred.hubcentral.room.create

import red.tetracube.homekitred.data.api.models.APIError
import red.tetracube.homekitred.data.db.datasource.HubDataSource
import red.tetracube.homekitred.data.db.datasource.RoomDataSource
import red.tetracube.homekitred.data.db.entities.RoomEntity
import red.tetracube.homekitred.business.models.errors.HomeKitRedError
import red.tetracube.homekitred.business.mappers.toDomainError

class RoomCreateUseCases(
    private val hubDatasource: HubDataSource,
    private val roomAPIRepository: RoomAPIRepository,
    private val roomDatasource: RoomDataSource
) {

    suspend fun createRoom(name: String): Result<Unit> {
        val hub = hubDatasource.getActiveHub()!!
        val createRoomResult = roomAPIRepository.createRoom(hub.apiURI, hub.token, name)
        if (createRoomResult.isFailure) {
            return Result.failure(
                createRoomResult.exceptionOrNull()
                    ?.let { it as APIError }
                    ?.toDomainError()
                    ?: HomeKitRedError.UnprocessableResult
            )
        }

        val createdRoomResponse = createRoomResult.getOrThrow()
        val roomEntity = RoomEntity(
            slug = createdRoomResponse.slug,
            name = createdRoomResponse.name,
            hubSlug = hub.slug
        )
        roomDatasource.insert(roomEntity)
        return Result.success(Unit)
    }

}