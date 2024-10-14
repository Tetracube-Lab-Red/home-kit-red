package red.tetracube.homekitred.usecases.hub

import red.tetracube.homekitred.data.api.models.APIError
import red.tetracube.homekitred.data.api.repositories.HubAPIRepository
import red.tetracube.homekitred.domain.HomeKitRedError
import red.tetracube.homekitred.domain.mappers.toDomainError

class CreateHub(
    private val hubAPIRepository: HubAPIRepository
) {

    suspend operator fun invoke(hubAddress: String, hubName: String, hubPassword: String): Result<String> {
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