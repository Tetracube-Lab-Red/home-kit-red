package red.tetracube.homekitred.data.services

import red.tetracube.homekitred.data.api.payloads.room.RoomResponse
import red.tetracube.homekitred.data.api.repositories.RoomAPIRepository
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
        // 1 get rooms
        val roomsAPIResult = roomAPIRepository.getRooms(
            apiURI,
            token
        )
        if (roomsAPIResult.isFailure) {
            // ToDo: ignoring error for now
        } else {
            val rooms = roomsAPIResult.getOrNull()?.rooms
                ?: emptyList<RoomResponse>()
            rooms.forEach { room ->
                var roomEntity = RoomEntity(
                    slug = room.slug,
                    name = room.name,
                    hubSlug = hubSlug
                )
                roomDatasource.insert(roomEntity)
            }
        }
        // 2 get notifications
        // 3 get devices
    }

}