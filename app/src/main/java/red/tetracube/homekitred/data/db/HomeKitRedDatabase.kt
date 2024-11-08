package red.tetracube.homekitred.data.db

import android.content.Context
import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import red.tetracube.homekitred.data.db.datasource.DeviceDataSource
import red.tetracube.homekitred.data.db.datasource.HubDatasource
import red.tetracube.homekitred.data.db.datasource.RoomDatasource
import red.tetracube.homekitred.data.db.entities.DeviceEntity
import red.tetracube.homekitred.data.db.entities.HubEntity
import red.tetracube.homekitred.data.db.entities.RoomEntity

@Database(
    entities = [
        HubEntity::class,
        RoomEntity::class,
        DeviceEntity::class
    ],
    version = 2,
    autoMigrations = [
        AutoMigration(from = 1, to = 2)
    ]
)
abstract class HomeKitRedDatabase : RoomDatabase() {
    abstract fun hubRepository(): HubDatasource
    abstract fun roomRepository(): RoomDatasource
    abstract fun deviceRepository(): DeviceDataSource

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
                        .build()

                    HOME_KIT_RED_DATABASE_INSTANCE = instance
                }
                return instance
            }
        }
    }
}