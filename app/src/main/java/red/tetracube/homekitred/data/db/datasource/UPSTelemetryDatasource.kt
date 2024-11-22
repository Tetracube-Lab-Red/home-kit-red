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

    @Query("SELECT * FROM ups_telemetry ORDER BY telemetry_ts desc")
    fun getLatest(): Flow<List<UPSTelemetryEntity>>

}