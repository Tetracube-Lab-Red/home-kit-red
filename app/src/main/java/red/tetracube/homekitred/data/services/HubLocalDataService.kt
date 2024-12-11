package red.tetracube.homekitred.data.services

import red.tetracube.homekitred.data.db.datasource.RoomDatasource
import red.tetracube.homekitred.data.db.entities.RoomEntity

class HubLocalDataService(
    private val roomAPIRepository: RoomAPIRepository,
    private val roomDatasource: RoomDatasource
) {

    suspend fun updateLocalHubData(
        hubSlug: String,
        apiURI: String,
        token: String
    ) {
        roomAPIRepository.getRooms(
            apiURI,
            token
        )
            .rooms
            .map { room ->
                RoomEntity(
                    slug = room.slug,
                    name = room.name,
                    hubSlug = hubSlug
                )
            }
            .forEach { roomEntity ->
                roomDatasource.insert(roomEntity)
            }
        // 2 get notifications
        // 3 get devices
    }

}