package red.tetracube.homekitred.data.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "hubs")
class HubEntity(
    @PrimaryKey var id: UUID,
    @ColumnInfo(name = "name") var name: String,
    @ColumnInfo(name = "active") var active: Boolean,
    @ColumnInfo(name = "token") var token: String,
    @ColumnInfo(name = "api_uri") var apiURI: String,
    @ColumnInfo(name = "websocket_uri") var websocketURI: String
)