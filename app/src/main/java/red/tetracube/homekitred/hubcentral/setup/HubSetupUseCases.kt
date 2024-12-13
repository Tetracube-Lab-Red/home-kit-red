package red.tetracube.homekitred.hubcentral.setup

import red.tetracube.homekitred.data.api.models.APIError
import red.tetracube.homekitred.data.api.datasource.HubAPIDataSource
import red.tetracube.homekitred.business.models.errors.HomeKitRedError
import red.tetracube.homekitred.business.mappers.toDomainError

class HubSetupUseCases(
    private val hubAPIRepository: HubAPIDataSource
) {

    suspend fun create(hubAddress: String, hubName: String, hubPassword: String): Result<String> {
        val createHubResult = hubAPIRepository.createHub(
            hubAddress,
            hubName,
            hubPassword
        )
        return if (createHubResult.isFailure) {
            Result.failure(
                createHubResult.exceptionOrNull()
                    ?.let { it as APIError }
                    ?.toDomainError()
                    ?: HomeKitRedError.UnprocessableResult
            )
        } else {
            Result.success(
                createHubResult.getOrNull()
                    ?.name
                    ?: "Invalid"
            )
        }
    }

}