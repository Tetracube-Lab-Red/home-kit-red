package red.tetracube.homekitred.app

import android.content.Context
import red.tetracube.homekitred.data.api.datasource.BaseAPIDataSource
import red.tetracube.homekitred.data.api.datasource.IoTAPIDataSource
import red.tetracube.homekitred.data.api.datasource.DeviceRoomAPIRepository
import red.tetracube.homekitred.data.api.datasource.HubDataSource
import red.tetracube.homekitred.data.api.datasource.RoomAPIRepository
import red.tetracube.homekitred.data.db.HomeKitRedDatabase

class HomeKitRedContainer(context: Context) {

    private val baseAPIDataSource by lazy { BaseAPIDataSource() }
    val hubAPIRepository by lazy { HubDataSource(baseAPIDataSource) }
    val roomAPIRepository by lazy { RoomAPIRepository(baseAPIDataSource) }
    val ioTAPIDataSource by lazy { IoTAPIDataSource(baseAPIDataSource) }
    val deviceRoomAPIRepository by lazy { DeviceRoomAPIRepository(baseAPIDataSource) }

    val homeKitRedDatabase by lazy { HomeKitRedDatabase.getInstance(context) }
}