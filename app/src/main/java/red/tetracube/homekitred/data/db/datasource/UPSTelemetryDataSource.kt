package red.tetracube.homekitred.data.db.datasource

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import red.tetracube.homekitred.data.db.entities.UPSTelemetryEntity
import java.util.UUID

@Dao
interface UPSTelemetryDataSource {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(telemetry: UPSTelemetryEntity): Long

    @Query("SELECT t.* FROM ups_telemetry t where t.telemetry_ts = (select max(t2.telemetry_ts) from ups_telemetry t2 where t2.device_id = :deviceId) LIMIT 1")
    suspend fun telemetryLiveData(deviceId: UUID): Flow<UPSTelemetryEntity>

    @Query("SELECT t.* FROM ups_telemetry t where t.telemetry_ts = (select max(t2.telemetry_ts) from ups_telemetry t2 where t2.device_id = t.device_id)")
    fun telemetriesLiveData(): Flow<List<UPSTelemetryEntity>>
}