package red.tetracube.homekitred.hubcentral.login

import red.tetracube.homekitred.business.models.errors.HomeKitRedError
import red.tetracube.homekitred.data.api.datasource.HubDataSource
import red.tetracube.homekitred.data.db.datasource.HubDatasource
import red.tetracube.homekitred.data.db.entities.HubEntity
import red.tetracube.homekitred.data.services.HubLocalDataService
import kotlin.String

class LoginUseCases(
    private val hubAPIRepository: HubDataSource,
    private val hubDataSource: HubDatasource,
    private val hubLocalDataService: HubLocalDataService
) {

    suspend fun login(
        hubAddress: String,
        hubName: String,
        hubPassword: String
    ): Result<Unit> {
        val hubDataAPI = try {
            hubAPIRepository.hubLogin(
                hubAddress,
                hubName,
                hubPassword
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
            slug = hubDataAPI.slug,
            name = hubDataAPI.name,
            token = hubDataAPI.token,
            apiURI = hubAddress,
            websocketURI = websocketURI,
            active = true
        )
        hubDataSource.insert(hubData)
        hubLocalDataService.updateLocalHubData(
            hubData.slug,
            hubData.apiURI,
            hubData.token
        )
        return Result.success(Unit)
    }

}