package red.tetracube.homekitred.data.api.repositories

import io.ktor.client.call.body
import io.ktor.client.network.sockets.ConnectTimeoutException
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.client.request.post
import io.ktor.http.HttpStatusCode
import kotlinx.serialization.SerializationException
import red.tetracube.homekitred.data.api.clients.TetraCubeAPIClient
import red.tetracube.homekitred.data.api.models.APIError
import red.tetracube.homekitred.data.api.payloads.hub.HubCreateRequest
import red.tetracube.homekitred.data.api.payloads.hub.HubInfo

class HubAPIRepository(
    private val tetraCubeAPIClient: TetraCubeAPIClient
) {

    companion object {
        const val CREATE_HUB = "/hub"
        const val GET_HUB_INFO_URL = "/hub/info"
    }

    suspend fun createHub(hubAddress: String, name: String, password: String) {
        val request = HubCreateRequest(name, password)
        val hubCreateResult = try {
            tetraCubeAPIClient.client.post("$hubAddress$CREATE_HUB")
            {
                request
            }
                .body<HubInfo>()
        } catch (clientException: ClientRequestException) {
            if (clientException.response.status == HttpStatusCode.Conflict) {
                APIError.EntityConflicts
            } else {
                APIError.ClientError
            }
        } catch (serverException: ServerResponseException) {
            APIError.ServerError
        } catch (connectionException: SerializationException) {
            APIError.UnprocessableReply
        } catch (timeoutException: ConnectTimeoutException) {
            APIError.RemoteUnreachable
        }
    }

    suspend fun getHubInfo(hubAddress: String, authToken: String) {
        val requestResult =
            tetraCubeAPIClient.client.get("$hubAddress$GET_HUB_INFO_URL")
            {
                headers {
                    append("Authorization", "Bearer $authToken")
                }
            }
        //.body() as HubInfo
        /* } catch (clientException: ClientRequestException) {
             APIDatasourceErrors.Unauthorized
         } catch (serverException: ServerResponseException) {
             APIDatasourceErrors.ServerError
         } catch (connectionException: Exception) {
             APIDatasourceErrors.RemoteUnreachable
         }

         return if (requestResult is APIDatasourceErrors) {
             Result.failure(requestResult)
         } else {
             Result.success(requestResult as HubInfo)
         }*/
    }

}