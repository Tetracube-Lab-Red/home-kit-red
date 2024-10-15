package red.tetracube.homekitred.app.container

import red.tetracube.homekitred.data.api.clients.TetraCubeAPIClient
import red.tetracube.homekitred.data.api.repositories.HubAPIRepository

class HomeKitRedContainer {

    private val tetraCubeAPIClient by lazy { TetraCubeAPIClient() }
    val hubAPIRepository by lazy { HubAPIRepository(tetraCubeAPIClient) }
}