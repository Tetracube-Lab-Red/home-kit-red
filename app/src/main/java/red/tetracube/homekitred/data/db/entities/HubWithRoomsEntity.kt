package red.tetracube.homekitred.data.db.entities

import androidx.room.Embedded
import androidx.room.Relation

data class HubWithRoomsEntity (
    @Embedded
    val hub: HubEntity,

    @Relation(
        parentColumn = "id",
        entityColumn = "hub_id"
    )
    val rooms: List<RoomEntity>
)