package red.tetracube.homekitred.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import red.tetracube.homekitred.data.db.converters.InstantConverter
import red.tetracube.homekitred.data.db.datasource.DeviceDatasource
import red.tetracube.homekitred.data.db.datasource.HubDatasource
import red.tetracube.homekitred.data.db.datasource.RoomDatasource
import red.tetracube.homekitred.data.db.datasource.UPSTelemetryDatasource
import red.tetracube.homekitred.data.db.entities.DeviceEntity
import red.tetracube.homekitred.data.db.entities.HubEntity
import red.tetracube.homekitred.data.db.entities.RoomEntity
import red.tetracube.homekitred.data.db.entities.UPSTelemetryEntity

@Database(
    entities = [
        HubEntity::class,
        RoomEntity::class,
        DeviceEntity::class,
        UPSTelemetryEntity::class
    ],
    version = 1,
    autoMigrations = [
    ]
)
@TypeConverters(InstantConverter::class)
abstract class HomeKitRedDatabase : RoomDatabase() {
    abstract fun hubRepository(): HubDatasource
    abstract fun roomRepository(): RoomDatasource
    abstract fun deviceRepository(): DeviceDatasource
    abstract fun upsTelemetryDatasource(): UPSTelemetryDatasource

    companion object {
        private const val DATABASE_FILE_NAME = "home_kit_red.db"

        @Volatile
        private var HOME_KIT_RED_DATABASE_INSTANCE: HomeKitRedDatabase? = null

        fun getInstance(context: Context): HomeKitRedDatabase {
            synchronized(this) {
                var instance = HOME_KIT_RED_DATABASE_INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        HomeKitRedDatabase::class.java,
                        DATABASE_FILE_NAME
                    )
                        .fallbackToDestructiveMigration()
                        .build()

                    HOME_KIT_RED_DATABASE_INSTANCE = instance
                }
                return instance
            }
        }
    }
}