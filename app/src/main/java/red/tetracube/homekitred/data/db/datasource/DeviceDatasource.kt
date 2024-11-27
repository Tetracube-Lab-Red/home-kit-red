package red.tetracube.homekitred.data.db.datasource

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import red.tetracube.homekitred.data.db.entities.DeviceEntity

@Dao
interface DeviceDatasource {

    @Query("SELECT * FROM devices WHERE slug = :slug LIMIT 1")
    suspend fun getDeviceBySlug(slug: String): DeviceEntity?

    @Query("SELECT * FROM devices WHERE hub_slug = :hubSlug")
    fun getDevices(hubSlug: String): Flow<DeviceEntity>

    @Query("SELECT * FROM devices WHERE hub_slug = :hubSlug")
    suspend fun getDevicesStaticList(hubSlug: String): List<DeviceEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(device: DeviceEntity): Long

    @Update(entity = DeviceEntity::class)
    suspend fun updateDevice(device: DeviceEntity)

}