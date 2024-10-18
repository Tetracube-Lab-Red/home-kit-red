package red.tetracube.homekitred.usecases.hub

import red.tetracube.homekitred.data.api.models.APIError
import red.tetracube.homekitred.data.api.repositories.HubAPIRepository
import red.tetracube.homekitred.data.db.datasource.HubDatasource
import red.tetracube.homekitred.data.db.entities.HubEntity
import red.tetracube.homekitred.domain.HomeKitRedError
import red.tetracube.homekitred.domain.mappers.toDomainError
import kotlin.String

class HubLogin(
    private val hubAPIRepository: HubAPIRepository,
    private val hubDataSource: HubDatasource
) {

    suspend operator fun invoke(
        hubAddress: String,
        hubName: String,
        hubPassword: String
    ): Result<Unit> {
        val getHubDataResult = hubAPIRepository.hubLogin(
            hubAddress,
            hubName,
            hubPassword
        )
        if (getHubDataResult.isFailure) {
            return Result.failure(
                getHubDataResult.exceptionOrNull()
                    ?.let { it as APIError }
                    ?.toDomainError()
                    ?: HomeKitRedError.UnprocessableResult
            )
        }
        val hubDataAPI = getHubDataResult.getOrThrow()
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
            active = true,
            id = null
        )
        hubDataSource.insert(hubData)
        return Result.success(Unit)
    }

}