package red.tetracube.homekitred.data.db.datasource

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import red.tetracube.homekitred.data.db.entities.DeviceEntity
import java.util.UUID

@Dao
interface DeviceDataSource {
    @Query("SELECT * FROM devices WHERE id = :id LIMIT 1")
    suspend fun getDeviceById(id: UUID): DeviceEntity?

    @Query("SELECT * FROM devices WHERE hub_id = :hubId")
    fun getDevices(hubId: UUID): Flow<DeviceEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(device: DeviceEntity): Long

    @Update(entity = DeviceEntity::class)
    suspend fun updateDevice(device: DeviceEntity)
}