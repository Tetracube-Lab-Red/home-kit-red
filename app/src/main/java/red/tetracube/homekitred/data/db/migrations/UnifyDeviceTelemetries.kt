package red.tetracube.homekitred.data.db.migrations

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

object UnifyDeviceTelemetries : Migration(3, 4) {

    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("DROP TABLE IF EXISTS device_scan_telemetry;")
        db.execSQL("DELETE FROM ups_telemetry")
        db.execSQL("ALTER TABLE ups_telemetry ADD connectivity_health TEXT NOT NULL")
        db.execSQL("ALTER TABLE ups_telemetry ADD telemetry_health TEXT NOT NULL")
    }

}