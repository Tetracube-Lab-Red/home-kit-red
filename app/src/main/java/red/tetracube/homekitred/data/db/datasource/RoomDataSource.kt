package red.tetracube.homekitred.data.db.datasource

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import red.tetracube.homekitred.data.db.entities.RoomEntity

@Dao
interface RoomDatasource {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(room: RoomEntity): Long

}