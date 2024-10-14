package red.tetracube.homekitred.usecases.hub

import red.tetracube.homekitred.data.api.repositories.HubAPIRepository

class CreateHub(
    val hubAPIRepository: HubAPIRepository
) {

    suspend operator fun invoke(hubAddress: String, hubName: String, hubPassword: String) {
        hubAPIRepository.createHub(
            hubAddress,
            hubName,
            hubPassword
        )
    }

}