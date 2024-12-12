package red.tetracube.homekitred.data.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import red.tetracube.homekitred.business.enumerations.DeviceType
import java.util.UUID

@Entity(tableName = "devices")
class DeviceEntity(
    @PrimaryKey var id: UUID,
    @ColumnInfo(name = "name") var name: String,
    @ColumnInfo(name = "type") var type: DeviceType,
    @ColumnInfo(name = "hub_id") var hubId: UUID,
    @ColumnInfo(name = "room_id") var roomId: UUID?,
)