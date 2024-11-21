package red.tetracube.homekitred.data.db.entities.converters

import androidx.room.TypeConverter
import java.time.Instant

class InstantConverter {

    @TypeConverter
    fun fromTimestamp(value: String?): Instant? {
        return value?.let { Instant.parse(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Instant?): String? {
        return date?.toString()
    }

}