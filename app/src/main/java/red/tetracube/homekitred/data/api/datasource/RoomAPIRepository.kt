package red.tetracube.homekitred.data.api.datasource

import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import red.tetracube.homekitred.data.api.entities.room.GetRoomsResponse
import red.tetracube.homekitred.data.api.entities.room.RoomCreateRequest
import red.tetracube.homekitred.data.api.entities.room.RoomResponse

class RoomAPIRepository(
    private val baseAPIDataSource: BaseAPIDataSource
) {

    companion object {
        const val CREATE_ROOM = "/hub/rooms"
        const val GET_ROOMS = "/hub/rooms"
    }

    suspend fun createRoom(
        hubAddress: String,
        token: String,
        name: String
    ): Result<RoomResponse> {
        val request = RoomCreateRequest(name)
        val hubBase = baseAPIDataSource.client.post("$hubAddress$CREATE_ROOM")
        {
            headers {
                append("Authorization", "Bearer $token")
            }
            setBody(request)
        }
            .body<RoomResponse>()
        return Result.success(hubBase)
    }

    suspend fun getRooms(
        hubAddress: String,
        token: String,
    ): GetRoomsResponse {
        return baseAPIDataSource.client.get("$hubAddress$GET_ROOMS")
        {
            headers {
                append("Authorization", "Bearer $token")
            }
        }
            .body<GetRoomsResponse>()
    }

}