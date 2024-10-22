package red.tetracube.homekitred.data.api.repositories

import io.ktor.client.call.body
import io.ktor.client.network.sockets.ConnectTimeoutException
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import kotlinx.serialization.SerializationException
import red.tetracube.homekitred.data.api.clients.TetraCubeAPIClient
import red.tetracube.homekitred.data.api.models.APIError
import red.tetracube.homekitred.data.api.payloads.room.RoomCreateRequest
import red.tetracube.homekitred.data.api.payloads.room.RoomCreateResponse

class RoomAPIRepository(
    private val tetraCubeAPIClient: TetraCubeAPIClient
) {

    companion object {
        const val CREATE_ROOM = "/hub/rooms"
    }

    suspend fun createRoom(hubAddress: String, token: String, name: String): Result<RoomCreateResponse> {
        val request = RoomCreateRequest(name)
        try {
            val hubBase = tetraCubeAPIClient.client.post("$hubAddress$CREATE_ROOM")
            {
                headersOf("Authorization", "Bearer $token")
                setBody(request)
            }
                .body<RoomCreateResponse>()
            return Result.success(hubBase)
        } catch (clientException: ClientRequestException) {
            return if (clientException.response.status == HttpStatusCode.Conflict) {
                Result.failure(APIError.EntityConflicts)
            } else {
                Result.failure(APIError.ClientError)
            }
        } catch (_: ServerResponseException) {
            return Result.failure(APIError.ServerError)
        } catch (_: SerializationException) {
            return Result.failure(APIError.UnprocessableReply)
        } catch (_: ConnectTimeoutException) {
            return Result.failure(APIError.RemoteUnreachable)
        } catch (_: HttpRequestTimeoutException) {
            return Result.failure(APIError.RemoteUnreachable)
        } catch (_: Exception) {
            return Result.failure(APIError.GenericAPIError)
        }
    }

}