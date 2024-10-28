package red.tetracube.homekitred.hubcentral.room.create

import red.tetracube.homekitred.data.api.models.APIError
import red.tetracube.homekitred.data.api.repositories.RoomAPIRepository
import red.tetracube.homekitred.data.db.datasource.HubDatasource
import red.tetracube.homekitred.data.db.datasource.RoomDatasource
import red.tetracube.homekitred.data.db.entities.RoomEntity
import red.tetracube.homekitred.app.exceptions.HomeKitRedError
import red.tetracube.homekitred.app.exceptions.mappers.toDomainError

class RoomCreateUseCases(
    private val hubDatasource: HubDatasource,
    private val roomAPIRepository: RoomAPIRepository,
    private val roomDatasource: RoomDatasource
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