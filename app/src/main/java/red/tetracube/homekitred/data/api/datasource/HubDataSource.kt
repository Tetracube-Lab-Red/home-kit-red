package red.tetracube.homekitred.data.api.datasource

import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import red.tetracube.homekitred.data.api.entities.hub.HubCreateRequest
import red.tetracube.homekitred.data.api.entities.hub.HubCreateResponse
import red.tetracube.homekitred.data.api.entities.hub.HubLoginRequest
import red.tetracube.homekitred.data.api.entities.hub.HubLoginResponse

class HubDataSource : BaseAPIDataSource() {

    companion object {
        const val BASE_PATH = "/hub"
        const val HUB_AUTH_URL = "/auth/login"
    }

    suspend fun createHub(apiURI: String, hubCreateRequest: HubCreateRequest): HubCreateResponse =
        client.post("$apiURI$BASE_PATH")
        {
            setBody(hubCreateRequest)
        }
            .body()

    suspend fun login(apiURI: String, hubLoginRequest: HubLoginRequest): HubLoginResponse =
        client.post("$apiURI$BASE_PATH$HUB_AUTH_URL")
        {
            setBody(hubLoginRequest)
        }
            .body()

}