package red.tetracube.homekitred.data.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "rooms")
data class RoomEntity(
    @PrimaryKey
    @ColumnInfo(name = "slug")
    var slug: String = "",

    @ColumnInfo(name = "name")
    var name: String = "",

    @ColumnInfo(name = "hub_slug")
    var hubSlug: String,
)