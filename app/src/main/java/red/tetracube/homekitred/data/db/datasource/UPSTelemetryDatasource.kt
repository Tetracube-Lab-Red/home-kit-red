package red.tetracube.homekitred.data.db.datasource

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import red.tetracube.homekitred.data.db.entities.UPSTelemetryEntity

@Dao
interface UPSTelemetryDatasource {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(telemetry: UPSTelemetryEntity): Long

    @Query("SELECT t.* FROM ups_telemetry t where t.telemetry_ts = (select max(t.telemetry_ts) from ups_telemetry t2 where t2.device_slug = t.device_slug)")
    fun getLatestTelemetries(): Flow<List<UPSTelemetryEntity>>

}