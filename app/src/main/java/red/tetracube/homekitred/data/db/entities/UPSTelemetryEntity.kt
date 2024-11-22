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
        Index(value = ["telemetry_ts", "device_slug"], unique = true)
    ]
)
class UPSTelemetryEntity {

    @PrimaryKey
    var id: UUID = UUID.randomUUID()

    @ColumnInfo(name = "out_frequency")
    var outFrequency: Float = Float.MIN_VALUE

    @ColumnInfo(name = "out_voltage")
    var outVoltage: Float = Float.MIN_VALUE

    @ColumnInfo(name = "out_current")
    var outCurrent: Float = Float.MIN_VALUE

    @ColumnInfo(name = "battery_voltage")
    var batteryVoltage: Float = Float.MIN_VALUE

    @ColumnInfo(name = "battery_runtime")
    var batteryRuntime: Long = Long.MIN_VALUE

    @ColumnInfo(name = "load")
    var load: Long = Long.MIN_VALUE

    @ColumnInfo(name = "temperature")
    var temperature: Float = Float.MIN_VALUE

    @ColumnInfo(name = "in_frequency")
    var inFrequency: Float = Float.MIN_VALUE

    @ColumnInfo(name = "in_voltage")
    var inVoltage: Float = Float.MIN_VALUE

    @ColumnInfo(name = "power_factor")
    var powerFactor: Float = Float.MIN_VALUE

    @ColumnInfo(name = "battery_charge")
    var batteryCharge: Float = Float.MIN_VALUE

    @ColumnInfo(name = "primary_status")
    var primaryStatus: UPSStatus = UPSStatus.NULL

    @ColumnInfo(name = "secondary_status")
    var secondaryStatus: UPSStatus? = null

    @ColumnInfo(name = "device_slug")
    var deviceSlug: String = ""

    @ColumnInfo(name = "connectivity_health")
    var connectivityHealth: ConnectivityHealth = ConnectivityHealth.UNREACHABLE

    @ColumnInfo(name = "telemetry_health")
    var telemetryHealth: TelemetryHealth = TelemetryHealth.NOT_TRANSMITTING

    @ColumnInfo(name = "telemetry_ts")
    var telemetryTS: Instant = Instant.MAX

}