package red.tetracube.homekitred.splash

import red.tetracube.homekitred.data.api.payloads.room.RoomResponse
import red.tetracube.homekitred.data.api.repositories.HubAPIRepository
import red.tetracube.homekitred.data.api.repositories.RoomAPIRepository
import red.tetracube.homekitred.data.db.datasource.HubDatasource
import red.tetracube.homekitred.data.db.datasource.RoomDatasource
import red.tetracube.homekitred.data.db.entities.RoomEntity
import red.tetracube.homekitred.splash.domain.mappers.toConnectInfo
import red.tetracube.homekitred.splash.domain.models.HubConnectionInfo

class SplashUseCases(
    private val hubDatasource: HubDatasource,
    private val hubAPI: HubAPIRepository,
    private val roomAPIRepository: RoomAPIRepository,
    private val roomDatasource: RoomDatasource
) {

    suspend fun getHubConnectInfo(): HubConnectionInfo? {
        return hubDatasource.getActiveHub()?.toConnectInfo()
    }

    suspend fun retrieveLatestHubInfo(hubConnectionInfo: HubConnectionInfo) {
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
        val roomsAPIResult = roomAPIRepository.getRooms(
            hubConnectionInfo.apiURI,
            hubConnectionInfo.token
        )
        if (roomsAPIResult.isFailure) {
            // ToDo: ignoring error for now
        } else {
            val rooms = roomsAPIResult.getOrNull()?.rooms
                ?: emptyList<RoomResponse>()
            rooms.forEach { room ->
                var roomEntity = RoomEntity(
                    id = null,
                    slug = room.slug,
                    name = room.name,
                    hubSlug = hubConnectionInfo.slug
                )
                roomDatasource.insert(roomEntity)
            }
        }
        // 2 get notifications
        // 3 get devices
    }

}