package red.tetracube.homekitred.data.api.datasource

import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import red.tetracube.homekitred.data.api.entities.hub.HubCreateRequest
import red.tetracube.homekitred.data.api.entities.hub.HubCreateResponse
import red.tetracube.homekitred.data.api.entities.hub.HubLoginRequest
import red.tetracube.homekitred.data.api.entities.hub.HubLoginResponse
import red.tetracube.homekitred.data.api.entities.hub.GetRoomsResponse
import red.tetracube.homekitred.data.api.entities.hub.RoomCreateRequest
import red.tetracube.homekitred.data.api.entities.hub.RoomData

class HubDataSource : BaseAPIDataSource() {

    companion object {
        const val BASE_PATH = "/hub"
        const val HUB_AUTH_URL = "/auth/login"
        const val ROOMS_URL = "/rooms"
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

    suspend fun createRoom(
        apiURI: String,
        token: String,
        request: RoomCreateRequest
    ): RoomData =
        client.post("$apiURI$BASE_PATH$ROOMS_URL")
        {
            headers {
                append("Authorization", "Bearer $token")
            }
            setBody(request)
        }
            .body()

    suspend fun getRooms(apiURI: String, token: String): GetRoomsResponse =
        client.get("$apiURI$BASE_PATH$ROOMS_URL")
        {
            headers {
                append("Authorization", "Bearer $token")
            }
        }
            .body()

}