package red.tetracube.homekitred.data.db

import androidx.room.TypeConverter
import java.time.Instant

class Converters {
    @TypeConverter
    fun fromStringToInstant(value: String?): Instant? {
        return value?.let { Instant.parse(it) }
    }

    @TypeConverter
    fun instantToString(date: Instant?): String? {
        return date?.toString()
    }
}