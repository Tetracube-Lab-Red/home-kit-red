package red.tetracube.homekitred.usecases.hub

import red.tetracube.homekitred.data.api.models.APIError
import red.tetracube.homekitred.data.api.repositories.HubAPIRepository
import red.tetracube.homekitred.data.db.datasource.HubDatasource
import red.tetracube.homekitred.domain.HomeKitRedError
import red.tetracube.homekitred.domain.HubData
import red.tetracube.homekitred.domain.mappers.toDomainError
import red.tetracube.homekitred.domain.mappers.toEntity
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
        val loginHubResult = hubAPIRepository.hubLogin(
            hubAddress,
            hubName,
            hubPassword
        )
        if (loginHubResult.isFailure) {
            return Result.failure(
                loginHubResult.exceptionOrNull()
                    ?.let { it as APIError }
                    ?.toDomainError()
                    ?: HomeKitRedError.UnprocessableResult
            )
        }
        val loginHubData = loginHubResult.getOrThrow()
        val websocketURI = if (hubAddress.startsWith("https")) {
            hubAddress.replace("https", "wss")
        } else if (hubAddress.startsWith("http")) {
            hubAddress.replace("http", "ws")
        } else {
            hubAddress
        }
        val hubData = HubData(
            slug = "",
            name = hubName,
            token = loginHubData.token,
            apiURI = hubAddress,
            websocketURI = websocketURI
        )
        hubDataSource.insert(hubData.toEntity(true))
        return Result.success(Unit)
    }

}