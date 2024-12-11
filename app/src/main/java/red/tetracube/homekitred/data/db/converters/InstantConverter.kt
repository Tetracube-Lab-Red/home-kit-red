package red.tetracube.homekitred.data.db.converters

import androidx.room.TypeConverter
import java.time.Instant

class InstantConverter {

    @TypeConverter
    fun fromLongToInstant(value: Long?): Instant? {
        return value?.let { Instant.ofEpochMilli(it) }
    }

    @TypeConverter
    fun instantToLong(date: Instant?): Long? {
        return date?.toEpochMilli()
    }

}