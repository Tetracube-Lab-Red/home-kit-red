package red.tetracube.homekitred.data.db.datasource

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import red.tetracube.homekitred.data.db.entities.RoomEntity

@Dao
interface RoomDatasource {

    @Query("SELECT * FROM rooms WHERE slug = :slug LIMIT 1")
    suspend fun getBySlug(slug: String): RoomEntity

    @Query("SELECT * FROM rooms WHERE hub_slug = :hubSlug order by name")
    suspend fun getHubRooms(hubSlug: String): List<RoomEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(room: RoomEntity): Long

}