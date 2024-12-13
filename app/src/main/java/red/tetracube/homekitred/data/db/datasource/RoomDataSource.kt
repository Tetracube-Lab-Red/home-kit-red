package red.tetracube.homekitred.data.db.datasource

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import red.tetracube.homekitred.data.db.entities.RoomEntity
import java.util.UUID

@Dao
interface RoomDataSource {
    @Query("SELECT * FROM rooms WHERE hub_id = :hubId order by name")
    suspend fun getHubRooms(hubId: UUID): List<RoomEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(room: RoomEntity): Long
}