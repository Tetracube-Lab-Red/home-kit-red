package red.tetracube.homekitred.data.db.entities

import androidx.room.Embedded
import androidx.room.Relation

data class HubWithRoomsEntity (
    @Embedded
    val hub: HubEntity,

    @Relation(
        parentColumn = "slug",
        entityColumn = "hub_slug"
    )
    val rooms: List<RoomEntity>
)