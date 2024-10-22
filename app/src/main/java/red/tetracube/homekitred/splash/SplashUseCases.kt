package red.tetracube.homekitred.splash

import red.tetracube.homekitred.data.api.repositories.HubAPIRepository
import red.tetracube.homekitred.data.db.datasource.HubDatasource
import red.tetracube.homekitred.data.db.datasource.RoomDatasource
import red.tetracube.homekitred.splash.domain.mappers.toConnectInfo
import red.tetracube.homekitred.splash.domain.models.HubConnectionInfo

class SplashUseCases(
    private val hubDatasource: HubDatasource,
    private val hubAPI: HubAPIRepository,
    private val roomDatasource: RoomDatasource
) {

    suspend fun getHubConnectInfo(): HubConnectionInfo? {
        return hubDatasource.getActiveHub()?.toConnectInfo()
    }

    suspend fun retrieveLatestHubInfo() {
        /*
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
         */
        // 1 get rooms
        // 2 get notifications
        // 3 get devices
    }

}