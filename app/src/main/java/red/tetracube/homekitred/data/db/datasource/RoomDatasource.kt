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

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(room: RoomEntity): Long

}