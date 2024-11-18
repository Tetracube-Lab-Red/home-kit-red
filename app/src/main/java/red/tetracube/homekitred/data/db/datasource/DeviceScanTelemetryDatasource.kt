package red.tetracube.homekitred.data.db.datasource

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import red.tetracube.homekitred.data.db.entities.DeviceScanTelemetryEntity

@Dao
interface DeviceScanTelemetryDatasource {

    @Insert()
    suspend fun insert(telemetryScan: DeviceScanTelemetryEntity): Long

    @Query("SELECT * FROM device_scan_telemetry WHERE device_slug = :slug ORDER BY telemetry_ts desc LIMIT 1")
    suspend fun getLatestByDevice(slug: String): DeviceScanTelemetryEntity

}