package red.tetracube.homekitred.business.usecases

import red.tetracube.homekitred.business.models.errors.HomeKitRedError
import red.tetracube.homekitred.data.db.datasource.HubDatasource
import red.tetracube.homekitred.data.db.entities.RoomEntity
import red.tetracube.homekitred.data.services.HubLocalDataService
import red.tetracube.homekitred.ui.shell.models.HubConnectionInfo

class GlobalDataUseCases(
    private val hubDatasource: HubDatasource,
) {

    suspend fun updateLocalData(
        hubSlug: String,
        apiURI: String,
        token: String
    ) {
         .getRooms(
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