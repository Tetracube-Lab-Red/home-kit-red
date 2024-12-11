package red.tetracube.homekitred.app

import android.content.Context
import red.tetracube.homekitred.data.api.clients.TetraCubeAPIClient
import red.tetracube.homekitred.data.api.repositories.DeviceAPIRepository
import red.tetracube.homekitred.data.api.repositories.DeviceRoomAPIRepository
import red.tetracube.homekitred.data.api.repositories.HubAPIRepository
import red.tetracube.homekitred.data.api.repositories.RoomAPIRepository
import red.tetracube.homekitred.data.db.HomeKitRedDatabase

class HomeKitRedContainer(context: Context) {

    private val tetraCubeAPIClient by lazy { TetraCubeAPIClient() }
    val hubAPIRepository by lazy { HubAPIRepository(tetraCubeAPIClient) }
    val roomAPIRepository by lazy { RoomAPIRepository(tetraCubeAPIClient) }
    val deviceAPIRepository by lazy { DeviceAPIRepository(tetraCubeAPIClient) }
    val deviceRoomAPIRepository by lazy { DeviceRoomAPIRepository(tetraCubeAPIClient) }

    val homeKitRedDatabase by lazy { HomeKitRedDatabase.getInstance(context) }
}