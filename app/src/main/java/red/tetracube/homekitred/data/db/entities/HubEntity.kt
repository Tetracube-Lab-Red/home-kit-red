package red.tetracube.homekitred.data.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "hubs")
class HubEntity(
    @PrimaryKey
    var id: Long?,

    @ColumnInfo(name = "slug")
    var slug: String = "",

    @ColumnInfo(name = "name")
    var name: String = "",

    @ColumnInfo(name = "active")
    var active: Boolean = false,

    @ColumnInfo(name = "token")
    var token: String = "",

    @ColumnInfo(name = "api_uri")
    var apiURI: String = "",

    @ColumnInfo(name = "websocket_uri")
    var websocketURI: String = ""
)