package red.tetracube.homekitred.data.db.datasource

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow
import red.tetracube.homekitred.data.db.entities.HubEntity
import red.tetracube.homekitred.data.db.entities.HubWithRoomsEntity

@Dao
interface HubDataSource {
    @Query("SELECT * FROM hubs WHERE active = true LIMIT 1")
    suspend fun getActiveHub(): HubEntity?

    @Query("SELECT * FROM hubs WHERE active = true LIMIT 1")
    fun streamActiveHub(): Flow<HubEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(hub: HubEntity): Long

    @Transaction
    @Query("SELECT * FROM hubs WHERE active = true LIMIT 1")
    fun getHubAndRooms(): Flow<HubWithRoomsEntity>
}