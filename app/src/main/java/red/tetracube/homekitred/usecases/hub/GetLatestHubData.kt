package red.tetracube.homekitred.usecases.hub

import red.tetracube.homekitred.data.api.models.APIError
import red.tetracube.homekitred.data.api.repositories.HubAPIRepository
import red.tetracube.homekitred.data.db.datasource.RoomDatasource
import red.tetracube.homekitred.data.db.entities.RoomEntity
import red.tetracube.homekitred.domain.HomeKitRedError
import red.tetracube.homekitred.domain.HubConnectionInfo
import red.tetracube.homekitred.domain.mappers.toDomainError

class GetLatestHubData(
    private val hubAPI: HubAPIRepository,
    private val roomDatasource: RoomDatasource
) {

    suspend operator fun invoke(hubConnectionInfo: HubConnectionInfo): Result<Unit> {
        val getHubInfoResult = hubAPI.getHubInfo(
            hubConnectionInfo.apiURI,
            hubConnectionInfo.token
        )
        if (getHubInfoResult.isFailure) {
            return Result.failure(
                getHubInfoResult.exceptionOrNull()
                    ?.let { it as APIError }
                    ?.toDomainError()
                    ?: HomeKitRedError.UnprocessableResult
            )
        }
        val hub = getHubInfoResult.getOrThrow()
        hub.rooms
            .map {
                RoomEntity(
                    id = null,
                    slug = it.slug,
                    name = it.name,
                    hubSlug = hub.slug
                )
            }
            .forEach { roomEntity ->
                roomDatasource.insert(roomEntity)
            }
        return Result.success(Unit)
    }

}