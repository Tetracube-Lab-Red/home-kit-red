package red.tetracube.homekitred.app.container

import android.content.Context
import red.tetracube.homekitred.data.api.clients.TetraCubeAPIClient
import red.tetracube.homekitred.data.api.repositories.HubAPIRepository
import red.tetracube.homekitred.data.db.HomeKitRedDatabase

class HomeKitRedContainer(context: Context) {

    private val tetraCubeAPIClient by lazy { TetraCubeAPIClient() }
    val hubAPIRepository by lazy { HubAPIRepository(tetraCubeAPIClient) }

    val homeKitRedDatabase by lazy { HomeKitRedDatabase.getInstance(context) }
}