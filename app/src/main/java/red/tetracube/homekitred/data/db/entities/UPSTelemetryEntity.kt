package red.tetracube.homekitred.data.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import red.tetracube.homekitred.data.enumerations.ConnectivityHealth
import red.tetracube.homekitred.data.enumerations.TelemetryHealth
import red.tetracube.homekitred.data.enumerations.UPSStatus
import java.time.Instant
import java.util.UUID

@Entity(
    tableName = "ups_telemetry",
    indices = [
        Index(value = ["telemetry_ts", "device_id"], unique = true)
    ]
)
class UPSTelemetryEntity(
    @PrimaryKey var id: UUID,
    @ColumnInfo(name = "out_frequency") var outFrequency: Float,
    @ColumnInfo(name = "out_voltage") var outVoltage: Float,
    @ColumnInfo(name = "out_current") var outCurrent: Float,
    @ColumnInfo(name = "battery_voltage") var batteryVoltage: Float,
    @ColumnInfo(name = "battery_runtime") var batteryRuntime: Long,
    @ColumnInfo(name = "load") var load: Long,
    @ColumnInfo(name = "temperature") var temperature: Float,
    @ColumnInfo(name = "in_frequency") var inFrequency: Float,
    @ColumnInfo(name = "in_voltage") var inVoltage: Float,
    @ColumnInfo(name = "power_factor") var powerFactor: Float,
    @ColumnInfo(name = "battery_charge") var batteryCharge: Float,
    @ColumnInfo(name = "primary_status") var primaryStatus: UPSStatus,
    @ColumnInfo(name = "secondary_status") var secondaryStatus: UPSStatus?,
    @ColumnInfo(name = "device_id") var deviceId: UUID,
    @ColumnInfo(name = "connectivity_health") var connectivityHealth: ConnectivityHealth,
    @ColumnInfo(name = "telemetry_health") var telemetryHealth: TelemetryHealth,
    @ColumnInfo(name = "telemetry_ts") var telemetryTS: Instant
)