package red.tetracube.homekitred.data.api.repositories

import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import red.tetracube.homekitred.data.api.clients.TetraCubeAPIClient
import red.tetracube.homekitred.data.api.payloads.hub.HubCreateRequest
import red.tetracube.homekitred.data.api.payloads.hub.HubCreateResponse
import red.tetracube.homekitred.data.api.payloads.hub.HubLoginAPI
import red.tetracube.homekitred.data.api.payloads.hub.LoginPayloadRequest

class HubAPIRepository(
    private val tetraCubeAPIClient: TetraCubeAPIClient
) {

    companion object {
        const val CREATE_HUB = "/hub"
        const val HUB_AUTH_URL = "/hub/auth/login"
    }

    suspend fun createHub(
        hubAddress: String,
        name: String,
        password: String
    ): Result<HubCreateResponse> {
        val request = HubCreateRequest(name, password)
        val hubBase = tetraCubeAPIClient.client.post("$hubAddress$CREATE_HUB")
        {
            setBody(request)
        }
            .body<HubCreateResponse>()
        return Result.success(hubBase)
    }

    suspend fun hubLogin(hubAddress: String, name: String, password: String): Result<HubLoginAPI> {
        val request = LoginPayloadRequest(name, password)
        val loginReply = tetraCubeAPIClient.client.post("$hubAddress$HUB_AUTH_URL")
        {
            setBody(request)
        }
            .body<HubLoginAPI>()
        return Result.success(loginReply)
    }

}