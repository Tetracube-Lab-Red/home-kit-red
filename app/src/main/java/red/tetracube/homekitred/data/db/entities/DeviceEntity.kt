package red.tetracube.homekitred.data.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "devices")
class DeviceEntity(
    @PrimaryKey
    @ColumnInfo(name = "slug")
    var slug: String = "",

    @ColumnInfo(name = "name")
    var name: String = "",

    @ColumnInfo(name = "type")
    var type: DeviceType = DeviceType.NONE,

    @ColumnInfo(name = "hub_slug")
    var hubSlug: String,

    @ColumnInfo(name = "room_slug")
    var roomSlug: String? = null,
) {
    enum class DeviceType {
        NONE,
        UPS,
        SWITCH,
        HUE
    }
}