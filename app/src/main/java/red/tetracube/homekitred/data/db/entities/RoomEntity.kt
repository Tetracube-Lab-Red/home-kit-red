package red.tetracube.homekitred.data.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "rooms")
class RoomEntity(
    @PrimaryKey var id: UUID,
    @ColumnInfo(name = "name") var name: String,
    @ColumnInfo(name = "hub_id") var hubId: UUID
)