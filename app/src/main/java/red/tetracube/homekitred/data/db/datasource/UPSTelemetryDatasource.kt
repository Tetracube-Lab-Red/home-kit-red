package red.tetracube.homekitred.data.db.datasource

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import red.tetracube.homekitred.data.db.entities.UPSTelemetryEntity

@Dao
interface UPSTelemetryDatasource {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(telemetry: UPSTelemetryEntity): Long

    @Query("SELECT * FROM ups_telemetry WHERE device_slug = :slug ORDER BY telemetry_ts desc LIMIT 1")
    suspend fun getLatest(slug: String): UPSTelemetryEntity

}