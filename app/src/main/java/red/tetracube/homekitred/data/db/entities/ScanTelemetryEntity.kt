package red.tetracube.homekitred.data.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import red.tetracube.homekitred.data.enumerations.ConnectivityStatus
import red.tetracube.homekitred.data.enumerations.TelemetryStatus
import java.time.Instant

import java.util.UUID

@Entity(tableName = "scan_telemetry")
class ScanTelemetryEntity {

    @PrimaryKey
    @ColumnInfo(name = "id")
    var id: UUID = UUID.randomUUID()

    @ColumnInfo(name = "connectivity")
    var connectivityStatus: ConnectivityStatus = ConnectivityStatus.UNREACHABLE

    @ColumnInfo(name = "telemetry_status")
    var telemetryStatus: TelemetryStatus = TelemetryStatus.NOT_TRANSMITTING

    @ColumnInfo(name = "telemetry_ts")
    var telemetryTS: Instant = Instant.MIN

    @ColumnInfo(name = "device_slug")
    var deviceSlug: String = ""

}
