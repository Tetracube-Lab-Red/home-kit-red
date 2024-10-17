package red.tetracube.homekitred.data.db.datasource

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import red.tetracube.homekitred.data.db.entities.HubEntity

@Dao
interface HubDatasource {

    @Query("SELECT * FROM hubs WHERE active = true LIMIT 1")
    suspend fun getActiveHub(): HubEntity?

    @Query("SELECT * FROM hubs WHERE active = true LIMIT 1")
    fun streamActiveHub(): Flow<HubEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(hub: HubEntity): Long

}