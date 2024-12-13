package red.tetracube.homekitred.business.usecases

import red.tetracube.homekitred.business.mappers.toConnectInfo
import red.tetracube.homekitred.business.models.errors.HomeKitRedError
import red.tetracube.homekitred.data.api.datasource.HubAPIDataSource
import red.tetracube.homekitred.data.api.entities.hub.HubLoginRequest
import red.tetracube.homekitred.data.db.datasource.HubDataSource
import red.tetracube.homekitred.data.db.entities.HubEntity

class HubUseCases(
    private val hubAPIDataSource: HubAPIDataSource,
    private val hubDataSource: HubDataSource,
    private val globalDataUseCases: GlobalDataUseCases
) {

    suspend fun login(
        hubAddress: String,
        hubName: String,
        hubPassword: String
    ): Result<Unit> {
        val hubDataAPI = try {
            hubAPIDataSource.login(
                hubAddress,
                hubLoginRequest = HubLoginRequest(
                    hubName,
                    hubPassword
                )
            )
        } catch (ex: HomeKitRedError) {
            return Result.failure(ex)
        }
        val websocketURI = if (hubAddress.startsWith("https")) {
            hubAddress.replace("https", "wss")
        } else if (hubAddress.startsWith("http")) {
            hubAddress.replace("http", "ws")
        } else {
            hubAddress
        }
        val hubData = HubEntity(
            id = hubDataAPI.id,
            name = hubDataAPI.name,
            token = hubDataAPI.token,
            apiURI = hubAddress,
            websocketURI = websocketURI,
            active = true
        )
        hubDataSource.insert(hubData)
        globalDataUseCases.updateLocalData(
            hubData.toConnectInfo()
        )
        return Result.success(Unit)
    }

}