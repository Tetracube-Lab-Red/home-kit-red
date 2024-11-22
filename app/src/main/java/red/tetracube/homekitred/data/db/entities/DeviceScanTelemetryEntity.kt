package red.tetracube.homekitred.data.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.time.Instant
import java.util.UUID

@Entity(
    tableName = "device_scan_telemetry",
    indices = [
        Index(value = ["telemetry_ts", "device_slug"], unique = true)
    ]
)
class DeviceScanTelemetryEntity {
    @PrimaryKey
    var id: UUID = UUID.randomUUID()



    @ColumnInfo(name = "telemetry_ts")
    var telemetryTS: Instant = Instant.MAX

    @ColumnInfo(name = "device_slug")
    var deviceSlug: String = ""
}